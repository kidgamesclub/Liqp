package liqp.filters;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

/**
 * Interface for performing filter lifecycle operations.  Instead of simply formatting the output, some filters may
 * want to communicate with other filters in the chain (maybe filters of the same type), and/or hijack the output
 * completely.
 */
public interface LFilter {

  String getName();

  void doFilter(FilterParams params,
                FilterChainPointer chain,
                RenderContext context,
                AtomicReference<Object> result);

  default void onStartChain(FilterParams params,
                            FilterChainPointer chain,
                            RenderContext context,
                            AtomicReference<Object> result) {

  }

  default void onFilterAction(FilterParams params,
                              FilterChainPointer chain,
                              RenderContext context,
                              AtomicReference<Object> result) {

  }

  default void onEndChain(FilterParams params,
                          FilterChainPointer chain,
                          RenderContext context,
                          AtomicReference<Object> result) {

  }
}
