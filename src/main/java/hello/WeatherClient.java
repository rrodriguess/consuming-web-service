package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import wsdl.*;

import java.text.SimpleDateFormat;

/**
 * Created by renato on 27/06/16.
 */
public class WeatherClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(WeatherClient.class);

    public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode) {
        GetCityForecastByZIP request = new GetCityForecastByZIP();
        request.setZIP(zipCode);

        log.info("Solicitando serviço para : " + zipCode);

        GetCityForecastByZIPResponse response = (GetCityForecastByZIPResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://wsf.cdyne.com/WeatherWS/Weather.asmx",
                        request,
                        new SoapActionCallback("http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP"));

        return response;
    }

    public void printResponse(GetCityForecastByZIPResponse response) {
        ForecastReturn forecastReturn = response.getGetCityForecastByZIPResult();

        if (forecastReturn.isSuccess()) {
            log.info("Previsão para " + forecastReturn.getCity() + "," + forecastReturn.getState());

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) {
                Temp temperature = forecast.getTemperatures();

                log.info(String.format("%s %s %s°-%s°", format.format(forecast.getDate().toGregorianCalendar().getTime()),
                        forecast.getDesciption(), temperature.getMorningLow(), temperature.getDaytimeHigh()));
                log.info("");
            }
        } else
            log.info("Sem previsão no momento");

    }


}
