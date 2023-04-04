package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.parser.data.StackOverflowLinkData;
import ru.tinkoff.edu.java.scrapper.api.client.HttpGitHubClient;
import ru.tinkoff.edu.java.scrapper.api.client.HttpStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.api.response.github.GitHubApiResponse;
import ru.tinkoff.edu.java.scrapper.api.response.stackoverflow.StackOverflowItemApiResponse;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
        public static void main(String[] args) {
                var ctx = SpringApplication.run(ScrapperApplication.class, args);
                ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
                System.out.println(config);
                WebClient webClient = WebClient.create();

                HttpGitHubClient gitHubClient = new HttpGitHubClient(webClient);
                GitHubLinkData userAndRepoGit = new GitHubLinkData("Manylovvv", "TinkoffEdu");
                GitHubApiResponse response = gitHubClient.GetRepo(userAndRepoGit);

                System.out.println(response);
                HttpStackOverflowClient stackOverflowClient = new HttpStackOverflowClient(webClient);
                StackOverflowLinkData QuestStack = new StackOverflowLinkData("1642028");
                StackOverflowItemApiResponse stackResponse =  stackOverflowClient.GetQuestion(QuestStack);
                System.out.println(stackResponse);
        }
}