fun countdown(n: Int) {
    for (i in n downTo 1) {
        println(i)
        Thread.sleep(10)
    }
    println("Start!")
}

countdown(10)























