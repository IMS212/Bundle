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
import java.net.URL;



import static net.fabricmc.installer.util.LauncherMeta.Version.pack_id;
import static net.fabricmc.installer.util.Utils.findDefaultInstallDir;


public class ClientInstaller {

	public static String install(File mcDir, String gameVersion, String loaderVersion, InstallerProgress progress) throws IOException {
		System.out.println("Installing " + gameVersion + " with fabric " + loaderVersion);

		String profileName = String.format("%s-%s-%s", gameVersion, Reference.LOADER_NAME, loaderVersion);

		MinecraftLaunchJson launchJson = Utils.getLaunchMeta(loaderVersion);
		launchJson.id = pack_id + "-" + profileName;
		launchJson.inheritsFrom = gameVersion;

		//Adds loader and the mappings
		launchJson.libraries.add(new MinecraftLaunchJson.Library(Reference.PACKAGE.replaceAll("/", ".") + ":" + Reference.MAPPINGS_NAME + ":" + gameVersion, Reference.mavenServerUrl));
		launchJson.libraries.add(new MinecraftLaunchJson.Library(Reference.PACKAGE.replaceAll("/", ".") + ":" + Reference.LOADER_NAME + ":" + loaderVersion, Reference.mavenServerUrl));
		final String FileURL = "https://github.com/jellysquid3/sodium-fabric/releases/download/mc1.16.3-0.1.0/sodium-fabric-mc1.16.3-0.1.0.jar";
		File destination = new File("%userprofile%\\AppData\\.minecraft\\mods\\sodium-fabric.jar");
		//FileUtils.copyURLToFile("https://raw.githubusercontent.com/IMS212/lithium-fabric/1.16.x/dev/Jenkinsfile", destination);

		try (BufferedInputStream in = new BufferedInputStream(new URL("https://github.com/IMS212/sodium-fabric/releases/download/iris/sodium-fabric-mc1.16.4-IRIS-SNAPSHOT.jar").openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\sodium-fabric.jar")) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }}
		try (BufferedInputStream in = new BufferedInputStream(new URL("https://github.com/jellysquid3/lithium-fabric/releases/download/mc1.16.4-0.6.0/lithium-fabric-mc1.16.4-0.6.0.jar").openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\lithium-fabric.jar")) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }}
		try (BufferedInputStream in = new BufferedInputStream(new URL("https://github.com/jellysquid3/phosphor-fabric/releases/download/mc1.16.2-v0.7.0/phosphor-fabric-mc1.16.3-0.7.0+build.10.jar").openStream()); FileOutputStream fileOutputStream = new FileOutputStream(findDefaultInstallDir() + "\\mods\\phosphor-fabric.jar")) {    byte dataBuffer[] = new byte[1024];    int bytesRead;    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {        fileOutputStream.write(dataBuffer, 0, bytesRead);    }}

		File versionsDir = new File(mcDir, "versions");
		File profileDir = new File(versionsDir, pack_id + "-" + profileName);
		File profileJson = new File(profileDir,   pack_id + "-" + profileName +  ".json");

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
}
