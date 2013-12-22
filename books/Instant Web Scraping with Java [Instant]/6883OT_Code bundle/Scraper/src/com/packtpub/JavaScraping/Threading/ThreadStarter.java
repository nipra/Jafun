package com.packtpub.JavaScraping.Threading;

class ThreadStarter {
    public static void main (String[] args) {
        WikiCrawler javaIsland = new WikiCrawler("/wiki/Java");
        WikiCrawler javaLanguage = new WikiCrawler("/wiki/Java_(programming_language)");
        javaLanguage.start();
        javaIsland.start(); 
    }
}