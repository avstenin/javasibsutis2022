package dns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PingTimeList {
    private static List<PingResult> queriesLog;

    public PingTimeList()
    {
        queriesLog = new ArrayList<PingResult>();
    }

    /**
     * Путём фильтрации исходного лога queriesLog извлекается запись с полем ip.
     * 
     * @param ip Домен DNS-сервера.
     * @return Запись с полем ip, если она существует, иначе null.
     */
    public PingResult findQuery(String ip)
    {
        return queriesLog.stream()
                         .filter( query -> ip.equals( query.getIp() ) )
                         .findAny()
                         .orElse(null);
    }

    /**
     * Если запись с полем ip существует, она удаляется из лога queriesLog.
     * 
     * @param ip Домен DNS-сервера.
     */
    public void remove(String ip)
    {
        PingResult query = findQuery(ip);
        if (query != null)
            queriesLog.remove(query);
    }

    /**
     * Методом на месте (через системную утилиту 'ping') измеряется среднее время, которое требуется для прохождения от текущего узла до сервера.
     * Информация затем записывается в лог queriesLog, если не было ошибок.
     * Для более подробной информации см. PingUtility.check(ip).
     * 
     * @param ip Домен DNS-сервера.
     * @throws IOException Ошибки при вводе/выводе в систему.
     * @throws InterruptedException Процесс с утилитой 'ping' был прерван.
     */
    public void add(String ip)
    throws IOException, InterruptedException
    {
        Optional<Float> timeMonad = PingUtility.check(ip);
        if (!timeMonad.isPresent())
            return;

        add(ip, timeMonad.get());
    }

    /**
     * Используя существующие данные, записывает информацию в лог queriesLog.
     * Если запись уже существует, она перезаписывается.
     * 
     * @param ip Домен DNS-сервера.
     * @param time Среднее время, которое требуется для прохождения от текущего узла до сервера.
     */
    public void add(String ip, float time)
    {
        PingResult query = findQuery(ip);
        if (query != null)
            queriesLog.remove(query);
        queriesLog.add(new PingResult(ip, time));
    }

    /**
     * @return Отсортированный в порядке убывания массив из результатов.
     */
    public PingResult[] getQueriesLog()
    {
        PingResult[] queriesArray = queriesLog.toArray(new PingResult[0]);
        Arrays.sort(queriesArray, Collections.reverseOrder());
        return queriesArray;
    }
}
