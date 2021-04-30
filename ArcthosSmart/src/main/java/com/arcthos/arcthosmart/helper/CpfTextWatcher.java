package com.arcthos.arcthosmart.helper;

import android.text.Editable;
import android.text.InputFilter;

import br.com.concretesolutions.canarinho.validator.Validador;
import br.com.concretesolutions.canarinho.validator.ValidadorCPFCNPJ;
import br.com.concretesolutions.canarinho.watcher.CPFCNPJTextWatcher;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 16/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class CpfTextWatcher extends CPFCNPJTextWatcher {
    private static final char[] CPF = "###.###.###-##".toCharArray();
    private static final InputFilter[] FILTRO_CPF = new InputFilter[]{new InputFilter.LengthFilter(CPF.length)};
    private final Validador validador = ValidadorCPFCNPJ.getInstance();
    private final Validador.ResultadoParcial resultadoParcial = new Validador.ResultadoParcial();

    @Override
    public void afterTextChanged(final Editable s) {
        if (isMudancaInterna()) {
            return;
        }
        s.setFilters(FILTRO_CPF);
        final StringBuilder builder = trataAdicaoRemocaoDeCaracter(s, CPF);
        atualizaTexto(validador, resultadoParcial, s, builder);
    }
}
