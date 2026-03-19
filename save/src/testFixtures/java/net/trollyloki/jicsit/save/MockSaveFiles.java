package net.trollyloki.jicsit.save;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public final class MockSaveFiles {
    private MockSaveFiles() {
    }

    public static final Map<String, String> INVALID = Map.ofEntries(
            Map.entry("invalid-save-files/header-underflow.sav", "Unexpected end of file"),
            Map.entry("invalid-save-files/invalid-header-version.sav", "Invalid header version: -42"),
            Map.entry("invalid-save-files/header-version-4.sav", "Unsupported header version: 4"),
            Map.entry("invalid-save-files/header-version-15.sav", "Unknown header version: 15"),
            Map.entry("invalid-save-files/string-underflow.sav", "Unexpected end of file"),
            Map.entry("invalid-save-files/invalid-string.sav", "Invalid null terminator: !"),
            Map.entry("invalid-save-files/visibility-underflow.sav", "Unexpected end of file"),
            Map.entry("invalid-save-files/invalid-is-partitioned-world.sav", "Invalid isPartitionedWorld value: 0"),
            Map.entry("invalid-save-files/invalid-hash.sav", "Invalid MD5Hash: 0"),
            Map.entry("invalid-save-files/hash-underflow.sav", "Unexpected end of file"),
            Map.entry("invalid-save-files/invalid-is-creative-mode-enabled.sav", "Invalid isCreativeModeEnabled value: 256")
    );

    public static final Map<String, MockSaveFileValues> VALUES = Map.ofEntries(
            // Header versions <=4 were only used during pre-early-access development

            // Header from save by MrPassiveAggressiv3: https://www.reddit.com/r/SatisfactoryGame/comments/b4c5nk/completed_milestones_end_game_resources_save/
            Map.entry("mock-save-files/header5.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            5,
                            new SaveHeader(
                                    17,
                                    66297,
                                    "header5",
                                    "Persistent_Level",
                                    "?startloc=Grass Fields?sessionName=ayyxd?Visibility=SV_Private",
                                    "ayyxd",
                                    Duration.parse("PT4H43M2S"),
                                    LocalDateTime.parse("2019-03-22T18:51:42.356").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            null,
                            (byte) 0,
                            0,
                            null,
                            0,
                            null
                    ),
                    Map.of("startloc", "Grass Fields", "sessionName", "ayyxd", "Visibility", "SV_Private"),
                    true,
                    null
            )),

            // Headers from saves by SMERKIN 5000: https://satisfactory.guru/playthroughs/read/index/id/24/name/Horizontal+Challenge
            Map.entry("mock-save-files/header6.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            6,
                            new SaveHeader(
                                    25,
                                    140083,
                                    "header6",
                                    "Persistent_Level",
                                    "?startloc=Grass Fields?sessionName=Horizontal Challenge?Visibility=SV_Private",
                                    "Horizontal Challenge",
                                    Duration.parse("PT208H16M26S"),
                                    LocalDateTime.parse("2021-03-14T07:16:19.965").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            null,
                            (byte) 0,
                            0,
                            null,
                            0,
                            null
                    ),
                    Map.of("startloc", "Grass Fields", "sessionName", "Horizontal Challenge", "Visibility", "SV_Private"),
                    true,
                    null
            )),
            Map.entry("mock-save-files/header7.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            7,
                            new SaveHeader(
                                    25,
                                    147217,
                                    "header7",
                                    "Persistent_Level",
                                    "?startloc=Grass Fields?sessionName=Horizontal Challenge - Update 4?Visibility=SV_FriendsOnly",
                                    "Horizontal Challenge - Update 4",
                                    Duration.parse("PT226H12M28S"),
                                    LocalDateTime.parse("2021-03-18T12:58:23.909").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            null,
                            (byte) 1,
                            38,
                            null,
                            0,
                            null
                    ),
                    Map.of("startloc", "Grass Fields", "sessionName", "Horizontal Challenge - Update 4", "Visibility", "SV_FriendsOnly"),
                    true,
                    null
            )),

            // Header from save by SMERKIN 5000: https://satisfactory.guru/playthroughs/read/index/id/23/name/Engine+Update+Experimental+%28Post+Update+4%29
            Map.entry("mock-save-files/header8.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            8,
                            new SaveHeader(
                                    25,
                                    158662,
                                    "header8",
                                    "Persistent_Level",
                                    "?startloc=Rocky Desert?sessionName=Engine Experimental?Visibility=SV_Private",
                                    "Engine Experimental",
                                    Duration.parse("PT594H17M46S"),
                                    LocalDateTime.parse("2021-06-28T11:35:49.674").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            null,
                            (byte) 0,
                            38,
                            "INVALID_METADATA",
                            0,
                            null
                    ),
                    Map.of("startloc", "Rocky Desert", "sessionName", "Engine Experimental", "Visibility", "SV_Private"),
                    false,
                    null
            )),

            // Header from save by SMERKIN 5000: https://satisfactory.guru/playthroughs/read/index/id/26/name/LOST
            Map.entry("mock-save-files/header9.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            9,
                            new SaveHeader(
                                    28,
                                    174799,
                                    "header9",
                                    "Persistent_Level",
                                    "?startloc=Northern Forest?sessionName=LOST?Visibility=SV_Private",
                                    "LOST",
                                    Duration.parse("PT413H18M42S"),
                                    LocalDateTime.parse("2021-12-06T09:33:19.896").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            null,
                            (byte) 0,
                            40,
                            "",
                            0,
                            null
                    ),
                    Map.of("startloc", "Northern Forest", "sessionName", "LOST", "Visibility", "SV_Private"),
                    true,
                    null
            )),

            // Header from save by Stin Archi: https://www.youtube.com/watch?v=qqURlDtafYE
            Map.entry("mock-save-files/header10.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            10,
                            new SaveHeader(
                                    36,
                                    211839,
                                    "header10",
                                    "Persistent_Level",
                                    "?startloc=Grass Fields?sessionName=Gateworld?Visibility=SV_Private",
                                    "Gateworld",
                                    Duration.parse("PT2634H49M28S"),
                                    LocalDateTime.parse("2023-06-06T18:56:51.234").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    true,
                                    false,
                                    false
                            ),
                            null,
                            (byte) 0,
                            40,
                            """
                                    {\r
                                    \t"Mods": [\r
                                    \t\t{\r
                                    \t\t\t"Reference": "AreaActions",\r
                                    \t\t\t"Name": "Area Actions",\r
                                    \t\t\t"Version": "1.6.7"\r
                                    \t\t},\r
                                    \t\t{\r
                                    \t\t\t"Reference": "FicsItCam",\r
                                    \t\t\t"Name": "FicsIt-Cam",\r
                                    \t\t\t"Version": "0.3.5"\r
                                    \t\t},\r
                                    \t\t{\r
                                    \t\t\t"Reference": "MicroManage",\r
                                    \t\t\t"Name": "Micro Manage",\r
                                    \t\t\t"Version": "1.1.0"\r
                                    \t\t},\r
                                    \t\t{\r
                                    \t\t\t"Reference": "SkyUI",\r
                                    \t\t\t"Name": "SkyUI",\r
                                    \t\t\t"Version": "1.0.4"\r
                                    \t\t},\r
                                    \t\t{\r
                                    \t\t\t"Reference": "UtilityMod",\r
                                    \t\t\t"Name": "UtilityMod",\r
                                    \t\t\t"Version": "0.7.1"\r
                                    \t\t}\r
                                    \t]\r
                                    }""",
                            1,
                            "ASoaLaPzk02Jz81dKH0AcQ"
                    ),
                    Map.of("startloc", "Grass Fields", "sessionName", "Gateworld", "Visibility", "SV_Private"),
                    true,
                    new ModMetadata(
                            0,
                            List.of(
                                    new Mod(
                                            "AreaActions",
                                            "Area Actions",
                                            "1.6.7"
                                    ),
                                    new Mod(
                                            "FicsItCam",
                                            "FicsIt-Cam",
                                            "0.3.5"
                                    ),
                                    new Mod(
                                            "MicroManage",
                                            "Micro Manage",
                                            "1.1.0"
                                    ),
                                    new Mod(
                                            "SkyUI",
                                            "SkyUI",
                                            "1.0.4"
                                    ),
                                    new Mod(
                                            "UtilityMod",
                                            "UtilityMod",
                                            "0.7.1"
                                    )
                            ),
                            null
                    )
            )),

            // Header versions 11-12 were only used during internal Update 8 development

            // Header from save by serisak: https://www.youtube.com/watch?v=aLYUIf8ebRs
            Map.entry("mock-save-files/header13.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            13,
                            new SaveHeader(
                                    42,
                                    273254,
                                    "header13",
                                    "Persistent_Level",
                                    "?Name=Player?startloc=DuneDesert?skiponboarding?sessionName=Swamptopia?Visibility=SV_Private?advancedGameSettings=FG.GameRules.StartingTier=DgAAAAQAAAAJAAAA,FG.GameRules.NoUnlockCost=AgAAAAQAAAABAAAA,FG.GameRules.SetGamePhase=DgAAAAQAAAAEAAAA,FG.GameRules.UnlockAllResearchSchematics=AgAAAAQAAAABAAAA,FG.GameRules.UnlockAllResourceSinkSchematics=AgAAAAQAAAABAAAA,FG.PlayerRules.NoBuildCost=AgAAAAQAAAABAAAA,FG.PlayerRules.FlightMode=AgAAAAQAAAABAAAA?SessionSettings=SML.ForceAllowCheats=AgAAAAQAAAAAAAAA",
                                    "Swamptopia",
                                    Duration.parse("PT710H24M56S"),
                                    LocalDateTime.parse("2024-10-13T21:12:16.801").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    true,
                                    false
                            ),
                            null,
                            (byte) 0,
                            40,
                            "{\"Version\":1,\"Mods\":[{\"Reference\":\"FicsItCam\",\"Name\":\"FicsIt-Cam\",\"Version\":\"0.3.8\"},{\"Reference\":\"SkyUI\",\"Name\":\"SkyUI\",\"Version\":\"1.0.11\"}],\"FullMapName\":\"/Game/FactoryGame/Map/GameLevel01/Persistent_Level.Persistent_Level\"}",
                            0,
                            "5E1BpUVgj3zzvOG9M7wl3Q"
                    ),
                    Map.of("Name", "Player", "startloc", "DuneDesert", "skiponboarding", "", "sessionName", "Swamptopia", "Visibility", "SV_Private", "advancedGameSettings", "FG.GameRules.StartingTier=DgAAAAQAAAAJAAAA,FG.GameRules.NoUnlockCost=AgAAAAQAAAABAAAA,FG.GameRules.SetGamePhase=DgAAAAQAAAAEAAAA,FG.GameRules.UnlockAllResearchSchematics=AgAAAAQAAAABAAAA,FG.GameRules.UnlockAllResourceSinkSchematics=AgAAAAQAAAABAAAA,FG.PlayerRules.NoBuildCost=AgAAAAQAAAABAAAA,FG.PlayerRules.FlightMode=AgAAAAQAAAABAAAA", "SessionSettings", "SML.ForceAllowCheats=AgAAAAQAAAAAAAAA"),
                    true,
                    new ModMetadata(
                            1,
                            List.of(
                                    new Mod(
                                            "FicsItCam",
                                            "FicsIt-Cam",
                                            "0.3.8"
                                    ),
                                    new Mod(
                                            "SkyUI",
                                            "SkyUI",
                                            "1.0.11"
                                    )
                            ),
                            "/Game/FactoryGame/Map/GameLevel01/Persistent_Level.Persistent_Level"
                    )
            )),

            // Header from save by Coffee Cup Studios: https://docs.google.com/document/d/1QM_45CwXjWXVvPST-Y6UNRFJC9DS90eg8VkiY5U8fO4/edit?tab=t.0
            Map.entry("mock-save-files/header14.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            14,
                            new SaveHeader(
                                    52,
                                    460533,
                                    "header14",
                                    "Persistent_Level",
                                    "?skiponboarding?SessionSettings=FicsitRemoteMonitoring.General.SplineSampleDistance=CgAAAAQAAAAAAJZC,RaceTimer.InstantCartDeploy=AgAAAAQAAAABAAAA,SML.ForceAllowCheats=AgAAAAQAAAAAAAAA",
                                    "Corporate Ladder",
                                    Duration.parse("PT372H55M21S"),
                                    LocalDateTime.parse("2025-12-19T00:07:10.884").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    true,
                                    true,
                                    true
                            ),
                            "Corporate Ladder 2025 with Mod",
                            (byte) 0x78,
                            40,
                            "{\"Version\":1,\"Mods\":[{\"Reference\":\"BetterVehicleCamera\",\"Name\":\"BetterVehicleCamera\",\"Version\":\"1.4.1\"},{\"Reference\":\"CoffeeCupStudiosMod01\",\"Name\":\"Coffee Cup Studios Event\",\"Version\":\"1.10.1\"},{\"Reference\":\"RaceTimer\",\"Name\":\"Race Timer\",\"Version\":\"2.1.0\"}],\"FullMapName\":\"/Game/FactoryGame/Map/GameLevel01/Persistent_Level.Persistent_Level\"}",
                            1,
                            "6pk920q5bZkgEHO4_JMi_g"
                    ),
                    Map.of("skiponboarding", "", "SessionSettings", "FicsitRemoteMonitoring.General.SplineSampleDistance=CgAAAAQAAAAAAJZC,RaceTimer.InstantCartDeploy=AgAAAAQAAAABAAAA,SML.ForceAllowCheats=AgAAAAQAAAAAAAAA"),
                    true,
                    new ModMetadata(
                            1,
                            List.of(
                                    new Mod(
                                            "BetterVehicleCamera",
                                            "BetterVehicleCamera",
                                            "1.4.1"
                                    ),
                                    new Mod(
                                            "CoffeeCupStudiosMod01",
                                            "Coffee Cup Studios Event",
                                            "1.10.1"
                                    ),
                                    new Mod(
                                            "RaceTimer",
                                            "Race Timer",
                                            "2.1.0"
                                    )
                            ),
                            "/Game/FactoryGame/Map/GameLevel01/Persistent_Level.Persistent_Level"
                    )
            )),

            // Headers from saves by me
            Map.entry("mock-save-files/utf16.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            14,
                            new SaveHeader(
                                    52,
                                    463028,
                                    "utf16",
                                    "Persistent_Level",
                                    "?skiponboarding",
                                    "⚠️⚠️⚠️",
                                    Duration.parse("PT0S"),
                                    LocalDateTime.parse("2026-03-18T22:40:10.060").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            "⚠️⚠️⚠️_autosave_0",
                            (byte) 80,
                            40,
                            "",
                            0,
                            "cOjofkdMiSTLqMOFuT4P9g"
                    ),
                    Map.of("skiponboarding", ""),
                    true,
                    null
            )),
            Map.entry("mock-save-files/empty-session-name.sav", new MockSaveFileValues(
                    new SaveFileInfo(
                            14,
                            new SaveHeader(
                                    52,
                                    463028,
                                    "empty-session-name",
                                    "Persistent_Level",
                                    "",
                                    "", // intentionally empty
                                    Duration.parse("PT1M10S"),
                                    LocalDateTime.parse("2026-03-18T22:45:40.281").atZone(ZoneId.of("America/New_York")).toInstant(),
                                    false,
                                    false,
                                    false
                            ),
                            "lol no session name",
                            (byte) 80,
                            40,
                            "",
                            0,
                            "v8Ka701JwI0ZItGtbrZBeg"
                    ),
                    Map.of(),
                    true,
                    null
            ))
    );

}
