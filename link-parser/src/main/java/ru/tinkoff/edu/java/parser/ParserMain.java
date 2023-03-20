package ru.tinkoff.edu.java.parser;


import ru.tinkoff.edu.java.parser.data.LinkData;
import ru.tinkoff.edu.java.parser.parsers.*;

public class ParserMain {

    public static void main(String[] args) {

        AbstractLinkParser gitHubParser = new GitHubLinkParser();
        AbstractLinkParser stackOverflowParser = new StackOverflowLinkParser();
        AbstractLinkParser vkparser = new VkLinkParser();
        AbstractLinkParser nullParser = new NullLinkParser();

        //chain of responsibility
        gitHubParser.setNext(stackOverflowParser);
        stackOverflowParser.setNext(vkparser);
        vkparser.setNext(nullParser);

        String[] urls = {
                //set links here
                "https://github.com/explore/email",
                "https://github.com/Manylovvv/sda",
                "https://vk.com/im?peers=c230_391576837_86889028_c182_488238545_c59_549308564_234353917"
        };

        for (String url : urls) {
            LinkData data = gitHubParser.parseLink(url);
            System.out.println(data);
        }
    }
}