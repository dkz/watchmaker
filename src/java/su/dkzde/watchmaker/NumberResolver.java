package su.dkzde.watchmaker;

/**
 * @author Dmitry Kozlov
 */
public interface NumberResolver {
    void resolve(int number, ResolutionHandler handler);
}
