# LearnByEar
Приложение позволяет глоссировать песни на разных языках и просматривать глоссы, сделанные другими пользователями. При регистрации нужно указать, в частности, родной язык (языки) и изучаемый язык (языки).

# Функции
## Прослушивание песни
Строки текста песни на языке оригинала чередуются с их переводами на родной язык пользователя; в настройках можно отключить отображение перевода.
При выделении слова приложение может показывать грамматику и производные слова (указывается начальная форма, можно посмотреть склонение / спряжение и т.п.; вхождения в текст родственных слов или этого же слова в других формах подсвечиваются).

##Глоссирование
Пользователь набирает название песни. Приложение пытается найти в интернете текст и трек. Если чего-то из этого нет, нужно загрузить вручную. 

Система по возможности уменьшает рутинную составляющую глоссирования. 
Для всех слов, которые распознал автоматический переводчик, устанавливаются значения по умолчанию и предлагаются варианты. 
Пользователь только просматривает готовый результат и убирает "шероховатости" автоматического перевода, а также добавляет комментарии.

## Что используем
* Перевод: [Microsoft Translator](https://msdn.microsoft.com/en-us/library/ff512408.aspx)
* База данных глоссированных текстов: SQLite - ?
* Тексты: парсинг [azlyrics.com]() - насколько это хорошо?
* Сервер - ?

#Разработчики
Голчин Роман, Хаятов Олег, Акулов Михаил (M3237)
