package su.dkzde.watchmaker;

import java.time.LocalDateTime;

/**
 * @author Dmitry Kozlov
 */
public interface Schedule {
    boolean isSatisfied(LocalDateTime date);
}
