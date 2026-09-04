package com.warlley.biblioteca.util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtil {


    public static String dataAtual(){
        // 1. Pega a data atual
        LocalDate dataAtual = LocalDate.now();

        // 2. Define o formato que você deseja (ex: padrão brasileiro)
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 3. Transforma a data em String

        return dataAtual.format(formatador);
    }
    public static String dataDevolucao() {
        // 1. Define o formato da String (padrão brasileiro)
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 2. Transforma a String recebida em um objeto LocalDate (Parse)
        LocalDate data = LocalDate.now();

        // 3. Soma 1 mês usando a lógica nativa do Java (cuida de viradas de ano e meses com 28/30/31 dias)
        LocalDate novaData = data.plusMonths(1);

        // 4. Transforma a data em String
        return data.format(formatador);
    }
}
