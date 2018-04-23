# hoplon-layout
Toolkit for layouts in Hoplon

## Clearfix

A simple div that can be placed after children to force the parent to clear both
the children and the clearfix div.

This provides the real DOM that CSS clearfix solutions simulate.

### Basic usage

`hoplon-layout.clearfix.hoplon/clearfix` will return a clearfix element.

## Resize observers

Consists of two components:

0. A polyfill for resize observers, courtesy of CLJSJS
0. In integration between Hoplon/Javelin and a resize observer

### Basic usage

`hoplon-layout.resize-observer.hoplon/el` will return an observed element.

`el` is implemented as `defelem` in hoplon so takes `attributes` and `children`.

Optional arguments:

- `:width` a cell to push the width of the element into
- `:height` a cell to push the height of the element into
- `:f` a fn to call for the returned element, defaults to `hoplon.core/div`

Note that a clearfix element will also be appended to the end of the element.

Without the clearfix element certain CSS layouts (notably flexbox) can break the
resize observer's ability to measure the height of children effectively.
