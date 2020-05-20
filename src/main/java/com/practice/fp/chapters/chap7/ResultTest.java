package com.practice.fp.chapters.chap7;

import com.practice.fp.commons.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.Function;

public class ResultTest {

    public static void main(String[] args) {

        System.out.println(2.001f - 1.11f);
        System.out.println((new BigDecimal(2.001f).subtract(new BigDecimal(1.11f))).setScale(3, RoundingMode.HALF_DOWN));
        System.out.println(toLocaleThousandSeparator.apply(new BigDecimal("1000000000.001")));
        System.out.println(toBigDecimalFromLocaleString(toLocaleThousandSeparator.apply(new BigDecimal("100000.001"))));

        Map<String, Customer> customers = new Map<String, Customer>()
                .put("customer1", new Customer("first1", "last1", "email1@g.com"))
                .put("customer2", new Customer("first2", "last2"))
                .put("customer3", new Customer("first3", "last3", "email3@g.com"));

        Result<String> result1 = Result.success("customer1");
        Result<String> email1 = result1.flatMap(customers::get).flatMap(Customer::getEmail);
        System.out.println(email1);

        Result<String> result2 = Result.success("customer2");
        Result<String> email2 = result2.flatMap(customers::get).flatMap(Customer::getEmail);
        System.out.println(email2);

        Result<String> result3 = Result.failure(new IOException("test exception"));
        Result<String> email3 = result3.flatMap(customers::get).flatMap(Customer::getEmail);
        System.out.println(email3);

        Result<String> result4 = Result.failure(new CustomerException("123123123123"));
        Result<String> email4 = result4.flatMap(customers::get).flatMap(Customer::getEmail);
        System.out.println(email4);
    }

    public static Function<BigDecimal, String> toLocaleThousandSeparator =
            number -> {
                Locale locale = new Locale("id", "ID");
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(locale);
                DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

                formatter.setDecimalFormatSymbols(symbols);
                return formatter.format(number);
            };

    public static BigDecimal toBigDecimalFromLocaleString(String number) {

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        BigDecimal convertedNumber = null;
        try {
            convertedNumber = new BigDecimal(nf.parse(number).toString());
        } catch (ParseException e) {
        }

        return convertedNumber;
    }
}
