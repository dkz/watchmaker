package su.dkzde.watchmaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Kozlov
 */
public class MapNamespaceResolver implements NamespaceResolver {
    private final Map<String, ResolvedElement> namespace;
    public MapNamespaceResolver(Map<String, ResolvedElement> namespace) {
        this.namespace = new HashMap<>(namespace);
    }
    @Override
    public void resolve(String id, ResolutionHandler visitor) {
        ResolvedElement element = namespace.get(id);
        if (element != null) {
            element.resolveTo(visitor);
        } else {
            throw new ResolutionException("Unknown identifier '" + id + "'");
        }
    }
}
