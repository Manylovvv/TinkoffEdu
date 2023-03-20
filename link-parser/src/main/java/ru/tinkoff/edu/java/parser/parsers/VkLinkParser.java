package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.data.LinkData;
import ru.tinkoff.edu.java.parser.data.VkLinkData;

public final class VkLinkParser extends AbstractLinkParser implements LinkParser {
    private static final String VK_HOST = "vk.com";
    @Override
    public LinkData parseLink(String vk_url) {
        if (vk_url.contains(VK_HOST)) {
            String[] parts = vk_url.split("/");
            if (parts.length >= 3) {
                String user_id = parts[3];
                return new VkLinkData(user_id);
            }
        }
        return handleNext(vk_url);
    }
}