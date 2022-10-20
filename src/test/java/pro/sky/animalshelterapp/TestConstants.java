package pro.sky.animalshelterapp;

import pro.sky.animalshelterapp.models.Client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestConstants {
    public static String DEFAULTURL;
    public static String DEFAULTURLFINDALLRECORDS = "/find-all-records";
    public static String DEFAULTURLFINDBYNAMEJOHN = "/find-client-data-by-name?name=John";
    public static String DEFAULTURLFINDCHATIDBYNAMEJOHN = "/find-chatId-by-client-name?name=John";
    public static Long DEFAULTCHATID = 1234567L;
    public static String DEFAULTURLFINDBYSTATUSTRUE = "/find-clients-by-status?status=true";
    public static Client CLIENT1 = new Client(1234567L, "John", "John Smith", "+71234567890");
    public static Client CLIENT2 = new Client(7654321L, "Anna", "Anna Gordon", "+70987654321");
    public static List<Client> CLIENTS = Arrays.asList(CLIENT1, CLIENT2);

}
