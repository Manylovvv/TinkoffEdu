package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.records.GitHubRecord;

public final class GitHubLinkParser extends AbstractLinkParser {
    /**
     * Шаблон выражения имеет две группы захвата в скобках. 1-ая username, 2-ая - repositoryname
     * - `^` соответствует началу строки
     * - `https://github.com/` соответствует буквальной строке "https://github.com/"
     * - `([\\w.-]+)` соответствует одному или нескольким символам слова,
     * точкам или дефисам и захватывает их в первой группе
     * - `/` соответствует символу косой черты
     * - `([\\w.-]+)` соответствует одному или нескольким символам слова,
     * точкам или дефисам и захватывает их во второй группе
     * - `/` соответствует символу косой черты
     */
    private static final String GITHUB_LINK_REGEX = "^https://github.com/([\\w.-]+)/([\\w.-]+)/";
    private static final Pattern PATTERN = Pattern.compile(GITHUB_LINK_REGEX);

    public GitHubLinkParser(AbstractLinkParser nextParser) {
        super(nextParser);
    }

    /**
     * Метод parseLink использует класс Pattern для сопоставления ссылки с регулярным выражением и
     * извлечения из нее имени пользователя и имени репозитория. Затем он возвращает новый объект GitHubRecord
     * с извлеченной информацией. Если ссылка не соответствует регулярному выражению, она вызывает метод `parseLink`
     * следующего парсера в цепочке, если он существует.
     */
    @Override
    public Record parseLink(String link) {
        Matcher matcher = PATTERN.matcher(link);
        if (matcher.find()) {
            String username = matcher.group(1);
            String repositoryName = matcher.group(2);
            return new GitHubRecord(username, repositoryName);
        }
        return nextParser != null ? nextParser.parseLink(link) : null;
    }
}
