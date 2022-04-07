Project Status

![Android Workflow](https://github.com/mohitb117/OMDB_API/actions/workflows/android.yml/badge.svg)

-----

HOW TO BUILD APK for Demo App

1. Use Android Studio:
   I used Android Studio Config: 
   
   Android Studio Arctic Fox | 2020.3.1 Beta 3
   Build #AI-203.7717.56.2031.7395685, built on May 24, 2021
   Runtime version: 11.0.10+0-b96-7281165 x86_64
   VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
   macOS 11.4
   GC: G1 Young Generation, G1 Old Generation
   Memory: 2048M
   Cores: 12
   Registry: ide.intellij.laf.enable.animation=true, external.system.auto.import.disabled=true, ide.balloon.shadow.size=0
   Non-Bundled Plugins: PlantUML integration, String Manipulation, com.mallowigi.idea, net.vektah.codeglance, zielu.gittoolbox, com.developerphil.adbidea, com.jetbrains.kmm, org.intellij.plugins.markdown, wu.seal.tool.jsontokotlin

2. Use Gradle: 
  a. Download and extract the project folder.
  b. Run `./gradlew :app:assemble` --> This will compile the app into $rootFolder/app/build/outputs/apk
  c. Run `./gradlew :app:installDebug` --> This will compile + install the app directly onto the Android device.
  d. Attached apk to the project to move quickly.
  e. Run `./gradlew check` --> Unit tests + Lint   
   
3. FAQ's

* I am seeing an error: 
  What went wrong:
  Could not determine the dependencies of task ':app:assemble'.
> Could not create task ':app:assembleRelease'.
> Could not create task ':app:lintVitalRelease'.
> SDK location not found. 
> Define location with an ANDROID_SDK_ROOT environment variable or by setting the sdk.dir path in your project's 
> local properties file at '/Users/<user-name>/Desktop/archive/local.properties'.

-- Please define location for android studio sdk as per instructions. 

------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Task: 
1. Create an Android native application that allows users to search for movies using the Open Movie Database search API. 
The requirements of the app are as follows:

SubTasks: 
 Two views

○ First tab provides a movie search results list view.
	■ Each search result must show the movie name, director, year, brief plot summary, and poster.: 
     ✅ DONE

	■ When a user selects a search result, show a detail view. Be creative with the display.
     ✅ DONE

	■ Users can select favorite movies from the search results: **DONE**
     ✅ DONE


	■ Favorite status is shown in the search results if a movie has been added to favorites.
     ✅ DONE


	■ The user is notified when unable to perform a search.
     ✅ DONE

○ Second view shows all favorite movies and persist favorites across app launches.
	● Be localized to English.
     ✅ DONE

	● Do NOT use a web view.
     ✅ DONE
