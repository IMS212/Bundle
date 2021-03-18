/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.installer.client;

import net.fabricmc.installer.util.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;


import static net.fabricmc.installer.client.ClientHandler.intelSodium;
import static net.fabricmc.installer.util.LauncherMeta.Version.pack_id;
import static net.fabricmc.installer.util.Utils.findDefaultInstallDir;


public class ClientInstaller {

	public static String install(File mcDir, String gameVersion, String loaderVersion, InstallerProgress progress) throws IOException {
		System.out.println("Installing " + gameVersion + " with fabric " + loaderVersion);

		String profileName = String.format("%s-%s-%s", gameVersion, Reference.LOADER_NAME, loaderVersion);

		//copyFileUsingChannel
		MinecraftLaunchJson launchJson = Utils.getLaunchMeta(loaderVersion);
		launchJson.id = pack_id + "-" + profileName;
		launchJson.inheritsFrom = gameVersion;

		//Adds loader and the mappings
		launchJson.libraries.add(new MinecraftLaunchJson.Library(Reference.PACKAGE.replaceAll("/", ".") + ":" + Reference.MAPPINGS_NAME + ":" + gameVersion, Reference.mavenServerUrl));
		launchJson.libraries.add(new MinecraftLaunchJson.Library(Reference.PACKAGE.replaceAll("/", ".") + ":" + Reference.LOADER_NAME + ":" + loaderVersion, Reference.mavenServerUrl));
		//final String FileURL = "https://github.com/jellysquid3/sodium-fabric/releases/download/mc1.16.3-0.1.0/sodium-fabric-mc1.16.3-0.1.0.jar";
		//File destination = new File("%userprofile%\\AppData\\.minecraft\\mods\\sodium-fabric.jar");
		//FileUtils.copyURLToFile("https://raw.githubusercontent.com/IMS212/lithium-fabric/1.16.x/dev/Jenkinsfile", destination);
		//Class c;
		//InputStream src = c.getResourceAsStream(res);
		//Files.copy(src, Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
		try (BufferedInputStream in = new BufferedInputStream(new URL("https://github.com/IMS212/connected-block-textures-mod/releases/download/rel/connected-block-textures-0.1.3+1.16.4.jar").openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\cbt.jar")) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }}
		//try (BufferedInputStream in = new BufferedInputStream(new URL("https://github.com/jellysquid3/lithium-fabric/releases/download/mc1.16.4-0.6.0/lithium-fabric-mc1.16.4-0.6.0.jar").openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\lithium-fabric.jar")) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }}
		//try (BufferedInputStream in = new BufferedInputStream(new URL("https://github.com/jellysquid3/phosphor-fabric/releases/download/mc1.16.2-v0.7.0/phosphor-fabric-mc1.16.3-0.7.0+build.10.jar").openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\phosphor-fabric.jar")) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }}

		File versionsDir = new File(mcDir, "versions");
		File profileDir = new File(versionsDir, pack_id + "-" + profileName);
		File profileJson = new File(profileDir, pack_id + "-" + profileName + ".json");


		if (!profileDir.exists()) {
			profileDir.mkdirs();
		}

		/*

		This is a fun meme

		The vanilla launcher assumes the profile name is the same name as a maven artifact, how ever our profile name is a combination of 2
		(mappings and loader). The launcher will also accept any jar with the same name as the profile, it doesnt care if its empty

		 */
		File dummyJar = new File(profileDir, pack_id + "-" + profileName + ".jar");
		dummyJar.createNewFile();

		Utils.writeToFile(profileJson, Utils.GSON.toJson(launchJson));

		progress.updateProgress(Utils.BUNDLE.getString("progress.done"));

		return profileName;
	}
	public static void installerForMods(String url, String modjar) {
		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\" + modjar)) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//"https://github.com/IMS212/connected-block-textures-mod/releases/download/rel/connected-block-textures-0.1.3+1.16.4.jar"
	}
public static void installMods() {

	installerForMods("https://github.com/FabricMC/fabric/releases/download/0.30.0%2B1.16/fabric-api-0.30.0+1.16.jar", "fapi.jar");
	installerForMods("https://github.com/TerraformersMC/ModMenu/releases/download/v1.16.3/modmenu-1.16.3.jar", "modmenu.jar");
	installerForMods("https://github.com/jellysquid3/phosphor-fabric/releases/download/mc1.16.2-v0.7.0/phosphor-fabric-mc1.16.3-0.7.0+build.10.jar", "phosphor.jar");
	if(intelSodium.isSelected()) {
		installerForMods("https://github.com/jellysquid3/sodium-fabric/releases/download/mc1.16.3-0.1.0/sodium-fabric-mc1.16.3-0.1.0.jar", "sodium.jar");
	}
	else {
		installerForMods("https://github.com/IMS212/connected-block-textures-mod/releases/download/rel/connected-block-textures-0.1.3+1.16.4.jar", "cbt.jar");
		installerForMods("https://github.com/IMS212/Indium/releases/download/ind/indium-1.0.0.jar", "indium.jar");
		installerForMods("https://github.com/IMS212/sodium-fabric/releases/download/234/sodium-fabric-mc1.16.4-0.1.1-SNAPSHOT.jar", "sodium.jar");
	}

}
}
	/**
	 * Copy a file from source to destination.
	 *
	 * @param source
	 *        the source
	 * @param destination
	 *        the destination
	 * @return True if succeeded , False if not
	 */

