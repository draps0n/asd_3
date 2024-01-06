Dany jest ciąg liczb naturalnych, a także wskaźnik P, który początkowo wskazuje na pierwszy element 
w ciągu. Dla tego ciągu zdefiniowane są dwie operacje: 

  • ADD – dodanie elementu o wartości X-1 na pozycję P+1, a następnie przesunięcie wskaźnika P o X 
    elementów w prawo, gdzie X jest wartością elementu znajdującego się na pozycji P.
    
  • DELETE – usunięcie elementu znajdującego się na pozycji P+1, a następnie przesunięcie wskaźnika P 
    o X elementów w prawo, gdzie X jest wartością usuniętego elementu. 

Gdy wskaźnik P wskazuje na element nieparzysty wykonywana jest operacja ADD, w przeciwnym 
wypadku wykonywana jest operacja DELETE. 
Przesuwanie wskaźnika P w prawo odbywa się w sposób cykliczny, czyli elementem następnym po 
elemencie ostatnim jest element pierwszy. 
Wyznacz ciąg liczb naturalnych powstały po wykonaniu k operacji na ciągu wejściowym.

Wejście: 

Dwa wiersze, z których pierwszy zawiera liczbę operacji (k), a drugi początkowy ciąg n liczb 
naturalnych, w którym kolejne elementy oddzielone są znakiem odstępu. 

Wyjście: 

Ciąg liczb naturalnych tworzący rozwiązanie zadania, w którym kolejne elementy oddzielone są 
znakiem odstępu, wypisany w sposób cykliczny poczynając od elementu, na który wskazuje wskaźnik 
P. 

Wymagania:

• Złożoność czasowa operacji O(klogn)

• Złożoność czasowa wczytania danych do wybranej struktury danych: O(nlogn)

• Złożoność pamięciowa: O(n).

• Program musi wczytywać dane wejściowe z pliku, którego lokalizacja podana jest w 
  pierwszym parametrze wywołania programu. 

• Jedyną rzeczą, którą program wypisuje, musi być wiersz zawierający odpowiedź. 

• Całość rozwiązania musi zawierać się w jednym pliku. 

• Zakładamy poprawność danych wejściowych.

• Zabronione jest korzystanie z gotowych rozwiązań z języka Java (bądź innych), takich jak 
  ArrayList czy StringBuilder. Wyjątkami są te rozwiązania, które służą do operacji 
  wejścia/wyjścia (np. Scanner w Javie). Dozwolone jest także użycie klasy String oraz metod 
  w niej zawartych.
