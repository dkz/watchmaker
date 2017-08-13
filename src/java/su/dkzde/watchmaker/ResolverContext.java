package su.dkzde.watchmaker;

/**
 * @author Dmitry Kozlov
 */
public class ResolverContext {
    public final NamespaceResolver namespace;
    public final NumberResolver numbers;
    public ResolverContext(NamespaceResolver namespace, NumberResolver numbers) {
        this.namespace = namespace;
        this.numbers = numbers;
    }
}
