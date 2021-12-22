package dk.lw.loantypesgateway;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ContentProcessor implements Processor
{


    public void process(Exchange exchange) throws Exception
    {
        String custom = exchange.getIn().getBody(String.class);

        List<LoanType> loanTypes = extractLoanTypes(custom);
        loanTypes = convertLoanInformation(loanTypes);
        String csv = generateCsv(loanTypes);

        exchange.getIn().setBody(csv);
    }

    private List<LoanType> extractLoanTypes(String custom){
        String[] cells = custom.replaceAll("\r\n",";").split(";");
        List<LoanType> loanTypes = new ArrayList<>();
        for(int i = 6; i<=24; i+=6){
            loanTypes.add(new LoanType(cells[i],Double.parseDouble(cells[i+1]),Double.parseDouble(cells[i+2]),Double.parseDouble(cells[i+3]),Double.parseDouble(cells[i+4]),Integer.parseInt(cells[i+5])));
        }
        return loanTypes;
    }


    private List<LoanType> convertLoanInformation(List<LoanType> loanTypes) throws JSONException, IOException {
        MoneyConvert moneyConvert = new MoneyConvert();

        double conversionRate = moneyConvert.convert();

        for(int i = 0; i<4; i++){
            loanTypes.get(i).setFee(loanTypes.get(i).getFee() * conversionRate);
            loanTypes.get(i).setMax_limit(loanTypes.get(i).getMax_limit() * conversionRate);
            loanTypes.get(i).setMin_limit(loanTypes.get(i).getMin_limit() * conversionRate);
        }
        return loanTypes;
    }

    private String generateCsv(List<LoanType> loanTypes){
        StringBuilder csv = new StringBuilder();
        csv.append("type;min_limit;max_limit;interest_rate;fee;Duration\r");
        for (LoanType loanType:loanTypes) {
            csv.append(loanType.getType() + ";");
            csv.append(loanType.getMin_limit() + ";");
            csv.append(loanType.getMax_limit() + ";");
            csv.append(loanType.getInterest_rate() + ";");
            csv.append(loanType.getFee() + ";");
            csv.append(loanType.getDuration()+ ";\n");
        }
        return csv.toString();
    }
}


