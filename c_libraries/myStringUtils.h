#ifndef MYSTRINGUTILS_H
#define MYSTRINGUTILS_H

#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>

/**
 * Concatena due stringhe
 * 
 * @param str1
 * @param str2
 * @return
 */
char* concat(const char* str1, const char* str2){
    char* result = (char*) malloc(strlen(str1) + strlen(str2) + 1);
    strcpy(result, str1);
    strcat(result, str2);
    return result;
}

/**
 * Estrae una sottostringa da una stringa
 * 
 * @param str
 * @param beginIndex
 * @param endIndex
 * @return sottostringa
 */
char* substring(const char* str, int beginIndex, int endIndex){
    char* result = (char*) malloc(endIndex - beginIndex + 1);
    strncpy(result, str + beginIndex, endIndex - beginIndex);
    result[endIndex - beginIndex] = '\0';
    return result;
}

/**
 * Estrae una sottostringa da una stringa
 * 
 * @param str
 * @param beginIndex
 * @return sottostringa
 */
char* substringFrom(const char* str, int beginIndex){
    return substring(str, beginIndex, strlen(str));
}

/**
 * Estrae un array di sottostringhe da una stringa dividendo la stringa in base a una regex
 * 
 * @param str
 * @param regex
 * @return array di sottostringhe
 */
char** split(const char* str, const char* regex, int* size){
    char* strCopy = (char*) malloc(strlen(str) + 1);
    strcpy(strCopy, str);
    char* token = strtok(strCopy, regex);
    char** result = (char**) malloc(sizeof(char*));
    int i = 0;
    while(token != NULL){
        result = (char**) realloc(result, sizeof(char*) * (i + 1));
        result[i] = (char*) malloc(strlen(token) + 1);
        strcpy(result[i], token);
        i++;
        token = strtok(NULL, regex);
    }
    *size = i;
    return result;
}

/**
 * Estrae un array di sottostringhe da una stringa dividendo la stringa in base a un carattere
 * 
 * @param str
 * @param separatorChar
 * @return
 */
char** splitByChar(const char* str, char separatorChar, int* size){
    char* strCopy = (char*) malloc(strlen(str) + 1);
    strcpy(strCopy, str);
    char* token = strtok(strCopy, &separatorChar);
    char** result = (char**) malloc(sizeof(char*));
    int i = 0;
    while(token != NULL){
        result = (char**) realloc(result, sizeof(char*) * (i + 1));
        result[i] = (char*) malloc(strlen(token) + 1);
        strcpy(result[i], token);
        i++;
        token = strtok(NULL, &separatorChar);
    }
    *size = i;
    return result;
}

/**
 * Restituisce la stringa senza la prima occorrenza di una sottostringa
 * 
 * @param str
 * @param searchStr
 * @return
 */
char* removeFirst(const char* str, const char* searchStr){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    char* index = strstr(result, searchStr);
    if(index != NULL){
        int beginIndex = index - result;
        int endIndex = beginIndex + strlen(searchStr);
        char* firstPart = substring(result, 0, beginIndex);
        char* secondPart = substringFrom(result, endIndex);
        result = concat(firstPart, secondPart);
        free(firstPart);
        free(secondPart);
    }
    return result;
}

/**
 * Restituisce la stringa senza l'ultima occorrenza di una sottostringa
 * 
 * @param str
 * @param searchStr
 * @return
 */
char* removeLast(const char* str, const char* searchStr){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    char* index = strstr(result, searchStr);
    if(index != NULL){
        int beginIndex = index - result;
        int endIndex = beginIndex + strlen(searchStr);
        char* firstPart = substring(result, 0, beginIndex);
        char* secondPart = substringFrom(result, endIndex);
        result = concat(firstPart, secondPart);
        free(firstPart);
        free(secondPart);
    }
    return result;
}

/**
 * Restituisce la stringa senza tutte le occorrenze di una sottostringa
 * 
 * @param str
 * @param searchStr
 * @return
 */
char* removeAll(const char* str, const char* searchStr){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    char* index = strstr(result, searchStr);
    while(index != NULL){
        int beginIndex = index - result;
        int endIndex = beginIndex + strlen(searchStr);
        char* firstPart = substring(result, 0, beginIndex);
        char* secondPart = substringFrom(result, endIndex);
        result = concat(firstPart, secondPart);
        free(firstPart);
        free(secondPart);
        index = strstr(result, searchStr);
    }
    return result;
}

/**
 * Capitalizza la prima lettera di una stringa
 * 
 * @param str
 * @return str capitalizzata
 */
char* capitalize(const char* str){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    result[0] = toupper(result[0]);
    return result;
}

/**
 * Decapitalizza la prima lettera di una stringa
 * 
 * @param str
 * @return str decapitalizzata
 */
char* uncapitalize(const char* str){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    result[0] = tolower(result[0]);
    return result;
}

/**
 * Capitalizza la prima lettera di ogni parola di una stringa
 * 
 * @param str
 * @return str capitalizzata
 */
char* capitalizeAll(const char* str){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    char* token = strtok(result, " ");
    while(token != NULL){
        int beginIndex = token - result;
        int endIndex = beginIndex + strlen(token);
        char* firstPart = substring(result, 0, beginIndex);
        char* secondPart = substringFrom(result, endIndex);
        char* capitalizedToken = capitalize(token);
        result = concat(firstPart, capitalizedToken);
        result = concat(result, secondPart);
        free(firstPart);
        free(secondPart);
        free(capitalizedToken);
        token = strtok(NULL, " ");
    }
    return result;
}

/**
 * Decapitalizza la prima lettera di ogni parola di una stringa
 * 
 * @param str
 * @return str decapitalizzata
 */
char* uncapitalizeAll(const char* str){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    char* token = strtok(result, " ");
    while(token != NULL){
        int beginIndex = token - result;
        int endIndex = beginIndex + strlen(token);
        char* firstPart = substring(result, 0, beginIndex);
        char* secondPart = substringFrom(result, endIndex);
        char* uncapitalizedToken = uncapitalize(token);
        result = concat(firstPart, uncapitalizedToken);
        result = concat(result, secondPart);
        free(firstPart);
        free(secondPart);
        free(uncapitalizedToken);
        token = strtok(NULL, " ");
    }
    return result;
}

/**
 * Controlla se una stringa contiene un'altra stringa (case INsensitive)
 * 
 * @param str
 * @param searchStr
 * @return true se la stringa contiene l'altra stringa, false altrimenti
 */
#include <ctype.h>

bool containsIgnoreCase(const char* str, const char* searchStr){
    char* strCopy = (char*) malloc(strlen(str) + 1);
    strcpy(strCopy, str);
    char* searchStrCopy = (char*) malloc(strlen(searchStr) + 1);
    strcpy(searchStrCopy, searchStr);
    
    for (int i = 0; strCopy[i]; i++) {
        strCopy[i] = tolower(strCopy[i]);
    }
    
    for (int i = 0; searchStrCopy[i]; i++) {
        searchStrCopy[i] = tolower(searchStrCopy[i]);
    }
    
    bool result = contains(strCopy, searchStrCopy);
    free(strCopy);
    free(searchStrCopy);
    return result;
}

/**
 * Controlla se una stringa contiene un'altra stringa (case sensitive)
 * 
 * @param str
 * @param searchStr
 * @return true se la stringa contiene l'altra stringa, false altrimenti
 */
bool contains(const char* str, const char* searchStr){
    return strstr(str, searchStr) != NULL;
}

/**
 * Restituisce la posizione della prima occorrenza di una stringa in un'altra stringa
 * 
 * @param str
 * @param searchStr
 * @return -1 se non viene trovata alcuna occorrenza, altrimenti la posizione della prima occorrenza
 */
int indexOf(const char* str, const char* searchStr){
    const char* index = strstr(str, searchStr);
    if(index != NULL){
        return (int)(index - str);
    }
    return -1;
}

/**
 * Restituisce la posizione della prima occorrenza di una stringa in un'altra stringa (case insensitive)
 * 
 * @param str
 * @param searchStr
 * @return -1 se non viene trovata alcuna occorrenza, altrimenti la posizione della prima occorrenza
 */
int indexOfIgnoreCase(const char* str, const char* searchStr){
    char* strCopy = (char*) malloc(strlen(str) + 1);
    strcpy(strCopy, str);
    char* searchStrCopy = (char*) malloc(strlen(searchStr) + 1);
    strcpy(searchStrCopy, searchStr);
    
    for (int i = 0; strCopy[i]; i++) {
        strCopy[i] = tolower(strCopy[i]);
    }
    
    for (int i = 0; searchStrCopy[i]; i++) {
        searchStrCopy[i] = tolower(searchStrCopy[i]);
    }
    
    int result = indexOf(strCopy, searchStrCopy);
    free(strCopy);
    free(searchStrCopy);
    return result;
}

/**
 * Conta quante volte una sottostringa è presente nella stringa di partenza
 * 
 * @param str
 * @param searchStr
 * @return Numero di volte che searchStr si trova in str
 */
int countMatches(const char* str, const char* searchStr){
    int count = 0;
    char* strCopy = (char*) malloc(strlen(str) + 1);
    strcpy(strCopy, str);
    char* index = strstr(strCopy, searchStr);
    while(index != NULL){
        count++;
        int beginIndex = index - strCopy;
        int endIndex = beginIndex + strlen(searchStr);
        char* firstPart = substring(strCopy, 0, beginIndex);
        char* secondPart = substringFrom(strCopy, endIndex);
        strCopy = concat(firstPart, secondPart);
        free(firstPart);
        free(secondPart);
        index = strstr(strCopy, searchStr);
    }
    return count;
}

/**
 * Sostituisce tutte le occorrenze di una stringa con un'altra stringa (case sensitive)
 * 
 * @param str
 * @param searchStr
 * @param replacement
 * @return la stringa modificata
 */
char* replace(const char* str, const char* searchStr, const char* replacement){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    char* index = strstr(result, searchStr);
    while(index != NULL){
        int beginIndex = index - result;
        int endIndex = beginIndex + strlen(searchStr);
        char* firstPart = substring(result, 0, beginIndex);
        char* secondPart = substringFrom(result, endIndex);
        result = concat(firstPart, replacement);
        result = concat(result, secondPart);
        free(firstPart);
        free(secondPart);
        index = strstr(result, searchStr);
    }
    return result;
}

/**
 * Controlla se una stringa è vuota
 * 
 * @param str
 * @return true se la stringa è vuota, false altrimenti
 */
bool isEmpty(const char* str){
    return strlen(str) == 0;
}

/**
 * Controlla se una stringa è vuota o null
 * 
 * @param str
 * @return true se la stringa è vuota o null, false altrimenti
 */
bool isBlank(const char* str){
    return isEmpty(str) || str == NULL;
}

/**
 * Controlla se una stringa è un numero
 * 
 * @param str
 * @return true se la stringa è un numero, false altrimenti
 */
bool isNumeric(const char* str){
    return isInteger(str) || isDecimal(str);
}

/**
 * Controlla se una stringa è un numero intero
 * 
 * @param str
 * @return true se la stringa è un numero intero, false altrimenti
 */
bool isInteger(const char* str){
    if(isBlank(str)){
        return false;
    }
    for(int i = 0; i < strlen(str); i++){
        if(!isdigit(str[i])){
            return false;
        }
    }
    return true;
}

/**
 * Controlla se una stringa è un numero decimale
 * 
 * @param str
 * @return true se la stringa è un numero decimale, false altrimenti
 */
bool isDecimal(const char* str){
    if(isBlank(str)){
        return false;
    }
    int dotCount = 0;
    for(int i = 0; i < strlen(str); i++){
        if(!isdigit(str[i]) && str[i] != '.'){
            return false;
        }
        if(str[i] == '.'){
            dotCount++;
        }
    }
    return dotCount == 1;
}

/**
 * Controlla se la stringa è solo composta da lettere
 * 
 * @param str
 * @return true se la stringa è solo composta da lettere, false altrimenti
 */
bool isAlpha(const char* str){
    if(isBlank(str)){
        return false;
    }
    for(int i = 0; i < strlen(str); i++){
        if(!isalpha(str[i])){
            return false;
        }
    }
    return true;
}

/**
 * Controlla se la stringa è solo composta da lettere e numeri
 * 
 * @param str
 * @return true se la stringa è solo composta da lettere e numeri, false altrimenti
 */
bool isAlphaNumeric(const char* str){
    if(isBlank(str)){
        return false;
    }
    for(int i = 0; i < strlen(str); i++){
        if(!isalnum(str[i])){
            return false;
        }
    }
    return true;
}

/**
 * Rimpiazza \ con \\ in una stringa
 * 
 * @param str
 * @return stringa con \ rimpiazzati da \\
 */
char* escapeBackslash(const char* str){
    return replace(str, "\\", "\\\\");
}

/**
 * Rimpiazza " con \" in una stringa
 * 
 * @param str
 * @return stringa con " rimpiazzati da \"
 */
char* escapeDoubleQuote(const char* str){
    return replace(str, "\"", "\\\"");
}

/**
 * Rimpiazza ' con \' in una stringa
 * 
 * @param str
 * @return stringa con ' rimpiazzati da \'
 */
char* escapeSingleQuote(const char* str){
    return replace(str, "'", "\\'");
}

/**
 * Rimpiazza \n con \\n in una stringa
 * 
 * @param str
 * @return stringa con \n rimpiazzati da \\n
 */
char* escapeNewLine(const char* str){
    return replace(str, "\n", "\\n");
}

/**
 * Rimpiazza \r con \\r in una stringa
 * 
 * @param str
 * @return stringa con \r rimpiazzati da \\r
 */
char* escapeCarriageReturn(const char* str){
    return replace(str, "\r", "\\r");
}

/**
 * Rimpiazza \t con \\t in una stringa
 * 
 * @param str
 * @return stringa con \t rimpiazzati da \\t
 */
char* escapeTab(const char* str){
    return replace(str, "\t", "\\t");
}

/**
 * Rimpiazza \b con \\b in una stringa
 * 
 * @param str
 * @return stringa con \b rimpiazzati da \\b
 */
char* escapeBackspace(const char* str){
    return replace(str, "\b", "\\b");
}

/**
 * Rimpiazza \f con \\f in una stringa
 * 
 * @param str
 * @return stringa con \f rimpiazzati da \\f
 */
char* escapeFormFeed(const char* str){
    return replace(str, "\f", "\\f");
}

/**
 * Rimuove gli spazi bianchi all'inizio e alla fine di una stringa
 * 
 * @param str
 * @return stringa senza spazi bianchi all'inizio e alla fine
 */
char* trim(const char* str){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    while(result[0] == ' '){
        result = substringFrom(result, 1);
    }
    while(result[strlen(result) - 1] == ' '){
        result = substring(result, 0, strlen(result) - 1);
    }
    return result;
}

/**
 * Inverte una stringa
 * 
 * @param str
 * @return stringa invertita
 */
char* reverse(const char* str){
    char* result = (char*) malloc(strlen(str) + 1);
    strcpy(result, str);
    int beginIndex = 0;
    int endIndex = strlen(result) - 1;
    while(beginIndex < endIndex){
        char temp = result[beginIndex];
        result[beginIndex] = result[endIndex];
        result[endIndex] = temp;
        beginIndex++;
        endIndex--;
    }
    return result;
}

/**
 * Controlla se una stringa è palindroma
 * 
 * @param str
 * @return true se la stringa è palindroma, false altrimenti
 */
bool isPalindrome(const char* str){
    char* strCopy = (char*) malloc(strlen(str) + 1);
    strcpy(strCopy, str);
    char* reversedStr = reverse(strCopy);
    bool result = strcmp(strCopy, reversedStr) == 0;
    free(strCopy);
    free(reversedStr);
    return result;
}

/**
 * Converte una stringa in un intero
 * 
 * @param str
 * @return intero
 */
int toInt(const char* str){
    return atoi(str);
}

/**
 * Converte una stringa in un long
 * 
 * @param str
 * @return long
 */
long toLong(const char* str){
    return atol(str);
}

/**
 * Converte una stringa in un float
 * 
 * @param str
 * @return float
 */
float toFloat(const char* str){
    return atof(str);
}

/**
 * Converte una stringa in un double
 * 
 * @param str
 * @return double
 */
double toDouble(const char* str){
    return strtod(str, NULL);
}

/**
 * Converte una stringa in un boolean
 * 
 * @param str
 * @return boolean
 */
bool toBoolean(const char* str){
    return strcmp(str, "true") == 0;
}

/**
 * Concatena una lista di stringhe
 * 
 * @param strings
 * @param size
 * @return stringa concatenata
 */
char* concatStrings(const char** strings, int size){
    char* result = (char*) malloc(1);
    strcpy(result, "");
    for(int i = 0; i < size; i++){
        result = concat(result, strings[i]);
    }
    return result;
}

/**
 * Concatena una lista di stringhe con un separatore
 * 
 * @param separator
 * @param strings
 * @param size
 * @return stringa concatenata
 */
char* concatWithSeparator(const char* separator, const char** strings, int size){
    char* result = (char*) malloc(1);
    strcpy(result, "");
    for(int i = 0; i < size; i++){
        result = concat(result, strings[i]);
        if(i < size - 1){
            result = concat(result, separator);
        }
    }
    return result;
}

/**
 * Restituisce tutte le permutazioni di una stringa
 * 
 * @param str
 * @param size
 * @return array di permutazioni
 */
char** getAllPermutations(const char* str, int* size){
    char** result = (char**) malloc(sizeof(char*));
    *size = 0;
    if(strlen(str) == 1){
        result = (char**) realloc(result, sizeof(char*) * (*size + 1));
        result[*size] = (char*) malloc(strlen(str) + 1);
        strcpy(result[*size], str);
        (*size)++;
        return result;
    }
    for(int i = 0; i < strlen(str); i++){
        char* firstPart = substring(str, 0, i);
        char* secondPart = substringFrom(str, i + 1);
        char* subStr = concat(firstPart, secondPart);
        int subSize = 0;
        char** subResult = getAllPermutations(subStr, &subSize);
        for(int j = 0; j < subSize; j++){
            result = (char**) realloc(result, sizeof(char*) * (*size + 1));
            result[*size] = (char*) malloc(strlen(str) + 1);
            strcpy(result[*size], concat(subResult[j], substring(str, i, i + 1)));
            (*size)++;
        }
        free(firstPart);
        free(secondPart);
        free(subStr);
        for(int j = 0; j < subSize; j++){
            free(subResult[j]);
        }
        free(subResult);
    }
    return result;
}

#endif /* MYSTRINGUTILS_H */
