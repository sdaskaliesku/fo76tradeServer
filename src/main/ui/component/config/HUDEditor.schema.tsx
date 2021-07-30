import {HUDEditorSchema} from "./HUDEditor";

export const hudEditorSchema: HUDEditorSchema = {
  "Elements": {
    "QuickLoot": {
      "label": "Quickloot",
      "id": "QuickLoot",
      "description": "Menu that shows when you roll over a container. Also includes some container interact prompts (eg Stash box).",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Frobber": {
      "label": "Frobber",
      "id": "Frobber",
      "description": "Includes lock prompts (like unlocking a door or interacting with NPCs)Includes lock prompts (like unlocking a door or interacting with NPCs)",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "RollOver": {
      "label": "RollOver",
      "id": "RollOver",
      "description": "Includes the interact prompts for most other things.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Compass": {
      "label": "Compass",
      "id": "Compass",
      "description": "Compass that is normally centered at the bottom.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "QuestTracker": {
      "label": "QuestTracker",
      "id": "QuestTracker",
      "description": "Quest tracker that is normally in the upper right.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Notification": {
      "label": "Notification",
      "id": "Notification",
      "description": "Includes messages like \"You lack the requirements to make this\". Also includes public event notifications.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "LeftMeter": {
      "label": "LeftMeter",
      "id": "LeftMeter",
      "description": "Health bar and rads indicator/taking rads text.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "StealthMeter": {
      "label": "StealthMeter",
      "id": "StealthMeter",
      "description": "The meter that appears when you crouch (Hidden/Danger etc).",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Announce": {
      "label": "Announce",
      "id": "Announce",
      "description": "Quest announcements/rewards.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "TeamPanel": {
      "label": "TeamPanel",
      "id": "TeamPanel",
      "description": "The list of teammates shown when on a team.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "FusionCoreMeter": {
      "label": "FusionCoreMeter",
      "id": "FusionCoreMeter",
      "description": "Fusion core meter when the power armor gauges UI is disabled.",
      "fields": [
        {
          "label": "Scale",
          "id": "Scale",
          "defaultValue": 1,
          "type": "NUMERIC",
          "step": 0.1,
          "min": 0,
          "max": 1.5
        },
        {
          "label": "X",
          "id": "X",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Y",
          "id": "Y",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "RightMeter": {
      "label": "RightMeter",
      "id": "RightMeter",
      "description": "Description in progress",
      "fields": [],
      "additionalElements": [
        {
          "Parts": {
            "label": "Parts",
            "id": "Parts",
            "description": "Description in progress",
            "fields": [],
            "additionalConfigs": [
              {
                "label": "APMeter",
                "id": "APMeter",
                "description": "Shows your current AP.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "ActiveEffects",
                "id": "ActiveEffects",
                "description": "Shows your current acive effects (Diseased etc).",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "HungerMeter",
                "id": "HungerMeter",
                "description": "The hunger bar.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "ThirstMeter",
                "id": "ThirstMeter",
                "description": "The thirst bar.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "AmmoCount",
                "id": "AmmoCount",
                "description": "Your current/total ammo counts.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "ExplosiveAmmoCount",
                "id": "ExplosiveAmmoCount",
                "description": "Your current grenade/mine total.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "FlashLightWidget",
                "id": "FlashLightWidget",
                "description": "Unused for now. Would be the indicator that displays when flashlight is on.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              },
              {
                "label": "Emote",
                "id": "Emote",
                "description": "Shows the animation of your last used emote.",
                "fields": [
                  {
                    "label": "Scale",
                    "id": "Scale",
                    "defaultValue": 1,
                    "type": "NUMERIC",
                    "step": 0.1,
                    "min": 0,
                    "max": 1.5
                  },
                  {
                    "label": "X",
                    "id": "X",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  },
                  {
                    "label": "Y",
                    "id": "Y",
                    "defaultValue": 0,
                    "type": "NUMERIC",
                    "step": 1,
                    "min": -10000,
                    "max": 10000
                  }
                ]
              }
            ]
          }
        }
      ]
    }
  },
  "Colors": {
    "HitMarkerTint": {
      "label": "HitMarkerTint",
      "id": "HitMarkerTint",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "RightMeters": {
      "label": "RightMeters",
      "id": "RightMeters",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "LeftMeters": {
      "label": "LeftMeters",
      "id": "LeftMeters",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RadsBarRGB",
          "id": "RadsBarRGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Notifications": {
      "label": "Notifications",
      "id": "Notifications",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "HudFrobber": {
      "label": "HudFrobber",
      "id": "HudFrobber",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "QuestTracker": {
      "label": "QuestTracker",
      "id": "QuestTracker",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "TopCenter": {
      "label": "TopCenter",
      "id": "TopCenter",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Announce": {
      "label": "Announce",
      "id": "Announce",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "BottomCenter": {
      "label": "BottomCenter",
      "id": "BottomCenter",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "CompassRGB",
          "id": "CompassRGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "CompassBrightness",
          "id": "CompassBrightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "CompassContrast",
          "id": "CompassContrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "CompassSaturation",
          "id": "CompassSaturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Center": {
      "label": "Center",
      "id": "Center",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Team": {
      "label": "Team",
      "id": "Team",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RadsBarRGB",
          "id": "RadsBarRGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "Floating": {
      "label": "Center",
      "id": "Center",
      "description": "Description in progress",
      "fields": [
        {
          "label": "RGB",
          "id": "RGB",
          "defaultValue": "881111",
          "type": "COLOR"
        },
        {
          "label": "Brightness",
          "id": "Brightness",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Contrast",
          "id": "Contrast",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        },
        {
          "label": "Saturation",
          "id": "Saturation",
          "defaultValue": 0,
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000
        }
      ]
    },
    "HUD": {
      "label": "HUD",
      "id": "HUD",
      "description": "Description in progress",
      "fields": [
        {
          "label": "CrosshairOpacity",
          "id": "CrosshairOpacity",
          "type": "NUMERIC",
          "step": 1,
          "min": -10000,
          "max": 10000,
          "description": "Description in progress",
          "defaultValue": 1
        },
        {
          "label": "EnableRecoloring",
          "id": "EnableRecoloring",
          "type": "BOOLEAN",
          "description": "Description in progress",
          "defaultValue": true
        },
        {
          "label": "ThirstHungerPercentShow",
          "id": "ThirstHungerPercentShow",
          "type": "BOOLEAN",
          "description": "Description in progress",
          "defaultValue": true
        },
        {
          "label": "AlwaysShowThirstHunger",
          "id": "AlwaysShowThirstHunger",
          "type": "BOOLEAN",
          "description": "Description in progress",
          "defaultValue": false
        },
        {
          "label": "EditMode",
          "id": "EditMode",
          "type": "BOOLEAN",
          "description": "Description in progress",
          "defaultValue": false
        },
        {
          "label": "CustomCrosshair",
          "id": "CustomCrosshair",
          "type": "BOOLEAN",
          "description": "Description in progress",
          "defaultValue": false
        },
        {
          "label": "TZMapMarkers",
          "id": "TZMapMarkers",
          "type": "BOOLEAN",
          "description": "Description in progress",
          "defaultValue": false
        }
      ]
    }
  }
}