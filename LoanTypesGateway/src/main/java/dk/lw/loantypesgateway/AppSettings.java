package dk.lw.loantypesgateway;

public class AppSettings {

    public static final String serviceName  = "LoanTypesGateway";

    public static final String source  = "loantypes/se_loantypes/";
    public static final String destination  = "loantypes/dk_loantypes/";

    public static final String currencyConverterURL = "https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=SEK&to=DKK&amount=1";
    public static final String currencyConverterKey = "INSERT KEY HERE";
    public static final String currencyConverterHost = "currency-converter5.p.rapidapi.com";
}
