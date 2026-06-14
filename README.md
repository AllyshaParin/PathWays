# TTTM2213 PENGATURCARAAN APLIKASI MUDAH ALIH

## 1. Student Profile
Name: NURALLYSHA AYUNI BINTI SHAPARIN
Matric No.: A216487
Programme: BACHELOR OF INFORMATION TECHNOLOGY WITH HONOUR
Instructor's Name: CIKGU IZWAN
Short Introduction & Learning Goals:  
I am an aspiring student mobile developer focused on building scalable, user-centric Android applications using modern architectural patterns. 
My goal throughout this semester was to master declarative UI building with Jetpack Compose, secure local-first data systems using Room, 
and cloud synchronization tools to build apps that solve real-world problems.
Chosen SDG: SDG 1 - No Poverty
One-Line Statement: Leveraging machine learning-driven career matching and offline-first data structures to eliminate localized unemployment
bottlenecks and uplift low-income communities.



## 2. Lab Submissions

🧪 Lab 1 — UI Components & Theming
Screenshots: 
YouTube Demo Link: https://youtu.be/Bfnkr-Ihqc8
Reflection:  
Transitioning from traditional XML layouts to Jetpack Compose required a massive shift in mindset. 
Learning how state drives UI dynamically allowed me to build the initial foundation of the PathWays interface. 
Figuring out modifier chaining and layout weights was a challenge, but it set the foundation for building responsive components.

🧪 Lab 2 — Component Interaction & State Management
Screenshots: 
YouTube Demo Link: https://youtu.be/yct7aAV3Pxk
Reflection:  
This lab focused heavily on remember, mutableStateOf, and state hoisting rules. I implemented interactive elements like the 
profile preferences and application option switches. Managing state changes across multiple nested composables taught me how 
to keep unidirectional data flows predictable.

🧪 Lab 3 — Material Design 3 Theming & Composables
Screenshots: 
YouTube Demo Link: https://youtu.be/S4jL-bp6I7Y
Reflection:  
I updated the app's interface using modern Material Design 3 (M3) features, specifically utilizing custom CardDefaults, ModalBottomSheet, 
and cohesive shape structures. I established the primary color palette (0xFF2E7D32 Green for growth and sustainability) to fit the professional 
tone required for a modern employment hub.

🧪 Lab 4 — Advanced Navigation Frameworks
Screenshots: 
YouTube Demo Link: https://youtu.be/nFaROp_MMCM
Reflection:  
Implemented NavHost, composable routes, and handled explicit bottom tab state tracking. The trickiest part was configuring type-safe argument 
parsing across screen transitions (e.g., passing encoded job titles and salary identifiers down to the review screen without breaking the navigation stack runtime).

🧪 Lab 5 — Room Database & Local Persistence
Screenshots: 
YouTube Demo Link: https://youtu.be/qDPf9kUaxlg
Reflection:  
Integrated an offline-first local-storage engine using Room. I designed entities, structured DAOs, and managed initialization 
patterns via an application context database builder. This guaranteed that job-seekers can save application histories directly onto device storage disks securely.



## 3. Project Submissions

🚀 Project 1: Foundational Architecture (Lab 4 + Working Navigation Flow)
Problem Statement:  
Marginalized communities suffer from severe economic stagnation due to fragmented visibility into regional job availability. Traditional job portals are bloated, 
confusing, and fail to map basic career entry points cleanly for low-income job seekers.
YouTube VSR Link: https://youtu.be/cRvSQi8570Y
GitHub Repository: https://github.com/AllyshaParin/PathWays
Lessons Learned:  
This phase taught me the vital importance of software clean architecture. Separating standard layout UI elements from internal routing stacks 
prevents bugs and makes expanding navigation flows down the line much easier.

🚀 Project 2: Advanced Integration (Lab 5 + Dynamic Features + Hardware Sensors + Cloud)
Problem Statement:  
Unemployed individuals often lack stable internet connections or professional assets (like headshots) needed to apply for jobs on the spot. They need an app 
that works seamlessly offline, handles profile creations via physical hardware camera sensors, and synchronizes data to the cloud automatically when a network 
becomes available.
YouTube VSR Link: https://youtu.be/7IGJwTtTq1E
GitHub Repository: https://github.com/AllyshaParin/PathWays/tree/A216487_CikguIzwan_Project2
Lessons Learned:  
This project wrapped everything together. Resolving silent crashes with Android's FileProvider permissions taught me how strict platform-level 
security policies can be. Synchronizing local Room transactions with Firebase Cloud Services demonstrated how real-world, industry-standard 
production applications manage data continuity.



## 4. Reflection & Learning Journey

Growth Track (Lab 1 to Project 2):  
Looking back at Lab 1, I started by hardcoding basic layout surfaces. By Project 2, I was structuring complex asynchronous Coroutine loops, manipulating 
system storage files, intercepting runtime hardware permissions, and processing local-to-cloud sync patterns. 
Key Challenges & Mitigations:  
The toughest obstacle was getting the device's hardware camera to work without throwing security exceptions. I fixed this by updating file_paths.xml directory 
mappings and building clean directory instantiation blocks inside StorageUtils.kt to force folder builds before requesting system image file URIs.
SDG Impact Realization:  
By prioritizing an offline-first application strategy, PathWays addresses SDG 1 (No Poverty) directly. It ensures users living in areas with poor internet 
connection can still build profiles, scan resumes, and queue up job applications offline without being locked out of economic opportunities.



## 5. Skills & Tech Stack

Here are the tools and frameworks I can now confidently implement:
Languages: Kotlin
UI Framework: Jetpack Compose, Material Design 3 (M3)
Asynchronous Engine: Kotlin Coroutines & Asynchronous State Flow
Architecture: MVVM Design Pattern, Repository Boundary Architecture
Local Persistence: Room Database, SQLite Core, Shared File Providers
Cloud Framework: Firebase Ecosystem Integration
Image Handling: Coil (Asynchronous Image Processing Library)
Version Control: Git & GitHub Workflow Actions



## 6. Resources & References

Third-Party Libraries: 
Coil-Compose: Image parsing framework.
Jetpack Navigation: Navigation management.
Core APIs: Android Hardware Camera Intent Subsystem APIs, Local Device Sandbox File System Providers.
AI Assistance Acknowledgement:  
Generative AI was used strategically during troubleshooting cycles—specifically to debug silent IllegalArgumentException permission issues caused by 
mismatched FileProvider authority tokens and to safely structure complex argument routes in Jetpack Compose.
