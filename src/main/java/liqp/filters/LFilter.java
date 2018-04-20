package liqp.filters;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import liqp.nodes.LNode;
import liqp.nodes.RenderContext;

/**
 * Interface for performing filter lifecycle operations.
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
