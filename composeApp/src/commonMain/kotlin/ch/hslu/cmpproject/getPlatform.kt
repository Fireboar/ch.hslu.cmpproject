package ch.hslu.cmpproject

enum class PlatformType { ANDROID, IOS, DESKTOP, WEB }

expect fun getPlatform(): PlatformType