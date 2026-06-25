package com.placeti.projetoExercicioIndividual.exceptions;

// REVISÃO: Essa classe não estende RuntimeException.
// Assim ela nunca pode ser lançada como exceção (throw new AtorNotFoundException() não compila).
// Corrija para: public class AtorNotFoundException extends RuntimeException { ... }
// e adicione um construtor que receba a mensagem de erro, igual ao FilmeNotFoundException.
public class AtorNotFoundException {
}
