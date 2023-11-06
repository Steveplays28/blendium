package io.github.steveplays28.blendium.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import io.github.steveplays28.blendium.client.BlendiumClient;
import io.github.steveplays28.blendium.client.config.user.BlendiumConfig;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import static io.github.steveplays28.blendium.client.BlendiumClient.MOD_ID;
import static io.github.steveplays28.blendium.client.BlendiumClient.MOD_LOADER_CONFIG_FOLDER_PATH;

public class BlendiumConfigLoader {
	public static final Path CONFIG_FOLDER_PATH = MOD_LOADER_CONFIG_FOLDER_PATH.resolve(MOD_ID);

	public static void load() {
		createMissingConfigurationFolders();

		var configurationFilePaths = BlendiumConfigurationFilePaths.class.getFields();
		var configurations = BlendiumConfigurations.class.getFields();

		for (int i = 0; i < configurationFilePaths.length; i++) {
			createMissingConfigurationFiles();

			try (InputStream stream = Files.newInputStream((Path) configurationFilePaths[i].get(Path.class))) {
				var gson = createGson();
				configurations[i].set(
						BlendiumConfigurations.class,
						gson.fromJson(
								new JsonReader(new BufferedReader(new InputStreamReader(stream))),
								configurations[i].getType()
						)
				);
			} catch (IOException | IllegalAccessException e) {
				BlendiumClient.LOGGER.error(
						"Unable to load Blendium configuration files at {}. Loading default configuration. See stack trace below:",
						CONFIG_FOLDER_PATH
				);
				e.printStackTrace();
			}
		}
	}

	public static void save() {
		var success = true;
		var configurationFilePaths = BlendiumConfigurationFilePaths.class.getFields();
		var configurations = BlendiumConfigurations.class.getFields();

		for (int i = 0; i < configurationFilePaths.length; i++) {
			try (FileWriter writer = new FileWriter(configurationFilePaths[i].get(Path.class).toString())) {
				var gson = createGson();
				var json = gson.toJson(configurations[i].get(BlendiumBaseConfig.class));
				writer.write(json);

			} catch (IOException | IllegalAccessException e) {
				BlendiumClient.LOGGER.error(
						"Unable to save Blendium configuration files at {}. See stack trace below:",
						CONFIG_FOLDER_PATH
				);
				e.printStackTrace();
				success = false;
			}
		}

		if (success) {
			BlendiumClient.LOGGER.info("Saved Blendium configuration files, located at {}", CONFIG_FOLDER_PATH);
		}
	}

	private static void createMissingConfigurationFolders() {
		try {
			if (!MOD_LOADER_CONFIG_FOLDER_PATH.toFile().exists()) {
				Files.createDirectory(MOD_LOADER_CONFIG_FOLDER_PATH);
			}

			if (!CONFIG_FOLDER_PATH.toFile().exists()) {
				Files.createDirectory(CONFIG_FOLDER_PATH);
			}
		} catch (IOException e) {
			BlendiumClient.LOGGER.error(
					"Unable to create Blendium configuration folder at {}. Loading default configuration. See stack trace below:",
					CONFIG_FOLDER_PATH
			);
			e.printStackTrace();
		}
	}

	private static @NotNull Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		return gsonBuilder.setPrettyPrinting().create();
	}

	private static void createMissingConfigurationFiles() {
		var configurationFilePaths = BlendiumConfigurationFilePaths.class.getFields();
		var defaultConfigurations = BlendiumDefaultConfigurations.class.getFields();

		for (int i = 0; i < configurationFilePaths.length; i++) {
			try {
				var configurationFilePath = ((Path) configurationFilePaths[i].get(Path.class));

				if (!configurationFilePath.toFile().exists()) {
					Files.createFile(configurationFilePath);
				}
			} catch (IOException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			try (FileWriter writer = new FileWriter(configurationFilePaths[i].get(Path.class).toString())) {
				if (((Path) configurationFilePaths[i].get(Path.class)).toFile().length() > 0) continue;

				var gson = createGson();
				var json = gson.toJson(defaultConfigurations[i].get(null));
				writer.write(json);
			} catch (IOException | IllegalAccessException e) {
				BlendiumClient.LOGGER.error(
						"Unable to create Blendium configuration files at {}. Loading default configuration. See stack trace below:",
						CONFIG_FOLDER_PATH
				);
				e.printStackTrace();
			}
		}

		BlendiumClient.LOGGER.info("Created Blendium configuration files, located at {}", CONFIG_FOLDER_PATH);
	}

	@SuppressWarnings("unused")
	public static class BlendiumConfigurationFilePaths {
		public static final Path CONFIG_FILE_PATH = Path.of(
				MessageFormat.format("{0}/{1}.json", CONFIG_FOLDER_PATH, MOD_ID));
	}

	@SuppressWarnings("unused")
	public static class BlendiumDefaultConfigurations {
		public static final BlendiumConfig DEFAULT_CONFIG = new BlendiumConfig();
	}

	@SuppressWarnings("unused")
	public static class BlendiumConfigurations {
		public static BlendiumConfig CONFIG;
	}
}
