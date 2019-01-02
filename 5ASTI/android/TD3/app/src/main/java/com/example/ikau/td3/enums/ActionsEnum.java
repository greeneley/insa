package com.example.ikau.td3.enums;

/**
 * La liste des actions d'AsyncFlickrTask pouvant être demandées par MainActivité.
 *
 * Chaque action est associée à une fonction et peut permettre à une tâche répétitive de la redéclencher.
 */
public enum ActionsEnum {
    NONE,
    PLAIN_JSON,
    TITLES,
    IMAGES,
    ADVANCED,
    FAVORITES
}
