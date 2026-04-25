# Platforma e-learning

## Descriere

Proiectul reprezinta o platforma e-learning simpla, dezvoltata in Java, care permite gestionarea cursurilor, utilizatorilor, cursantilor, instructorilor, lectiilor, quiz-urilor si rezultatelor obtinute de cursanti.

Aplicatia ruleaza in consola si foloseste un meniu interactiv. Utilizatorul poate alege actiuni precum afisarea cursurilor, inscrierea cursantilor la cursuri, adaugarea de lectii, cautarea unui curs dupa cod sau afisarea rezultatelor obtinute la quiz-uri.

Datele sunt gestionate in memorie, folosind servicii si colectii Java.

---

## Actiuni disponibile in sistem

1. Afisarea tuturor cursantilor
2. Afisarea tuturor instructorilor
3. Afisarea tuturor cursurilor disponibile
4. Afisarea cursurilor sortate dupa titlu
5. Cautarea unui curs dupa cod
6. Inscrierea unui cursant la un curs
7. Afisarea cursantilor inscrisi la un anumit curs
8. Afisarea lectiilor unui curs
9. Afisarea quiz-urilor unui curs
10. Afisarea rezultatelor unui cursant
11. Afisarea clasamentului pentru un quiz
12. Adaugarea unui cursant nou
13. Adaugarea unui curs nou
14. Adaugarea unei lectii la un curs
15. Adaugarea unui rezultat pentru un cursant la un quiz
16. Stergerea unui cursant dupa id
17. Stergerea unui curs dupa cod
18. Stergerea unui instructor dupa id

---

## Obiecte principale

1. Persoana
2. User
3. Cursant
4. Instructor
5. Curs
6. Lectie
7. Quiz
8. Intrebare
9. Inscriere
10. RezultatQuiz
11. CodCurs

---

## Structura proiectului

Proiectul este organizat in pachete:

Pachetul `model` contine clasele care descriu entitatile din aplicatie.

Pachetul `service` contine clasele care gestioneaza operatiile principale ale sistemului.

Pachetul `exception` contine exceptiile custom folosite in proiect.

---

## Functionalitati implementate

Aplicatia permite lucrul cu mai multe tipuri de date: cursanti, instructori, cursuri, lectii, quiz-uri si rezultate.

Pentru organizarea datelor sunt folosite colectii precum `List`, `Set`, `Map` si `TreeSet`.

Cursurile pot fi sortate dupa titlu, iar cautarea cursurilor dupa cod si a userilor dupa id se face rapid folosind map-uri.

Aplicatia foloseste si exceptii custom pentru cazurile in care un curs sau un user nu este gasit.

---

## Observatii

La pornire, aplicatia initializeaza cateva date de test, precum cursanti, instructori, cursuri, lectii si quiz-uri.

Dupa initializare, utilizatorul poate interactiona cu aplicatia prin meniul din consola.