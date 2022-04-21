package dns;

/**
 * Исключение, получаемое при парсинге неправильного списка ( это могут виды типа [], [;;;] и другие ), из которого нельзя извлечь элементы.
 */
public class ListParsingError
extends RuntimeException
{
    public ListParsingError() {
        super();
    }

    public ListParsingError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
