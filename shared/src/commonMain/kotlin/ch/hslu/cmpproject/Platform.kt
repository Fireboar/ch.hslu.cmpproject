package ch.hslu.cmpproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform