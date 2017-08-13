package su.dkzde.watchmaker;

/**
 * @author Dmitry Kozlov
 */
public interface NamespaceResolver {
    void resolve(String id, ResolutionHandler visitor);
}
