package com.ll.hogamapp.standard.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hogamapp.standard.appConfig.AppConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class Ut {
    public static class file {
        public static Optional<String> getExtensionByStringHandling(String filename) {
            return Optional.ofNullable(filename)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        }

        public static String copyToTempFile(String sourceFilePath) {
            String ext = Ut.file.getExtensionByStringHandling(sourceFilePath).orElse("tmp");

            String targetFilePath = genTempFilePathWithExt(ext);

            return copy(sourceFilePath, targetFilePath);
        }

        public static String genTempFilePath(String dirPath, String ext) {
            return (dirPath + "/" + UUID.randomUUID() + "." + ext).replace('\\', '/');
        }

        public static String genTempFilePathWithExt(String ext) {
            return genTempFilePath(getTempFileDirPath(), ext);
        }

        public static String genTempFilePath(String dirPath) {
            return genTempFilePath(dirPath, "temp");
        }

        private static String getTempFileDirPath() {
            return System.getProperty("java.io.tmpdir");
        }

        public static String genTempFilePath() {
            return genTempFilePath(getTempFileDirPath());
        }

        @SneakyThrows
        public static String copy(String sourceFilePath, String targetFilePath) {
            RandomAccessFile sourceFile = new RandomAccessFile(sourceFilePath, "r");
            RandomAccessFile newFile = new RandomAccessFile(targetFilePath, "rw");

            FileChannel source = sourceFile.getChannel();
            FileChannel target = newFile.getChannel();

            source.transferTo(0, source.size(), target);

            return targetFilePath;
        }

        public static void delete(String filePath) {
            if (filePath == null) return;
            new File(filePath).delete();
        }

        @SneakyThrows
        private static void rename(String sourceFilePath, String targetFilePath) {
            if (new File(sourceFilePath).renameTo(new File(targetFilePath)) == false) {
                copy(sourceFilePath, targetFilePath);
                delete(sourceFilePath);
            }
        }
    }

    @SneakyThrows
    public static void sleep(int millis) {
        Thread.sleep(millis);
    }

    public static class date {
        @SneakyThrows
        public static int parseDurationSecondsStringToInt(String input) {
            int indexOfSlash = input.indexOf("/");

            if (indexOfSlash != -1) {
                int duration = Integer.parseInt(input.substring(0, indexOfSlash));
                int scale = Integer.parseInt(input.substring(indexOfSlash + 1));

                return duration * 1000 / scale;
            }

            if (input.contains(":")) {
                List<String> split = Arrays.asList(input.split(":"));
                Collections.reverse(split);
                return IntStream.range(0, split.size())
                        .map(i -> Integer.parseInt(split.get(i)) * (int) Math.pow(60, i))
                        .sum() * 100;
            }

            return Integer.parseInt(input);
        }

        public static LocalDateTime bitsToLocalDateTime(List<Integer> bits) {
            return LocalDateTime.of(bits.get(0), bits.get(1), bits.get(2), bits.get(3), bits.get(4), bits.get(5), bits.get(6));
        }

        public static int getEndDayOf(int year, int month) {
            String yearMonth = year + "-" + "%02d".formatted(month);

            return getEndDayOf(yearMonth);
        }

        public static int getEndDayOf(String yearMonth) {
            LocalDate convertedDate = LocalDate.parse(yearMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            convertedDate = convertedDate.withDayOfMonth(
                    convertedDate.getMonth().length(convertedDate.isLeapYear()));

            return convertedDate.getDayOfMonth();
        }

        public static LocalDateTime parse(String pattern, String dateText) {
            return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
        }

        public static LocalDateTime parse(String dateText) {
            return parse("yyyy-MM-dd HH:mm:ss.SSSSSS", dateText);
        }
    }

    private static ObjectMapper getObjectMapper() {
        return (ObjectMapper) AppConfig.getContext().getBean("objectMapper");
    }

    public static String nf(long number) {
        return String.format("%,d", (int) number);
    }

    public static String getTempPassword(int length) {
        int index = 0;
        char[] charArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            index = (int) (charArr.length * Math.random());
            sb.append(charArr[index]);
        }

        return sb.toString();
    }

    public static class json {

        public static String toStr(Object obj) {
            try {
                return getObjectMapper().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Map<String, Object> toMap(String jsonStr) {
            try {
                return getObjectMapper().readValue(jsonStr, LinkedHashMap.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Map<String, Object> toMap(Object obj) {
            return toMap(toStr(obj));
        }
    }

    public static <K, V> Map<K, V> mapOf(String... args) {
        return mapOf((Object[]) args);
    }

    public static <K, V> Map<K, V> mapOf(Object... args) {
        Map<K, V> map = new LinkedHashMap<>();

        int size = args.length / 2;

        for (int i = 0; i < size; i++) {
            int keyIndex = i * 2;
            int valueIndex = keyIndex + 1;

            K key = (K) args[keyIndex];
            V value = (V) args[valueIndex];

            map.put(key, value);
        }

        return map;
    }

    public static class url {
        public static boolean isUrl(String url) {
            if (url == null) return false;
            return url.matches("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
        }

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (url.contains("?") == false) {
                url += "?";
            }

            if (url.endsWith("?") == false && url.endsWith("&") == false) {
                url += "&";
            }

            url += paramName + "=" + paramValue;

            return url;
        }

        public static String modifyQueryParam(String url, String paramName, String paramValue) {
            url = deleteQueryParam(url, paramName);
            url = addQueryParam(url, paramName, paramValue);

            return url;
        }

        public static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");
            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }

            String urlAfter = url.substring(startPoint + endPoint + 1);

            return url.substring(0, startPoint) + urlAfter;
        }

        public static String encode(String str) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }

        public static String getQueryParamValue(String url, String paramName, String defaultValue) {
            String[] urlBits = url.split("\\?", 2);

            if (urlBits.length == 1) {
                return defaultValue;
            }

            urlBits = urlBits[1].split("&");

            String param = Arrays.stream(urlBits)
                    .filter(s -> s.startsWith(paramName + "="))
                    .findAny()
                    .orElse(paramName + "=" + defaultValue);

            String value = param.split("=", 2)[1].trim();

            return value.length() > 0 ? value : defaultValue;
        }
    }
}
