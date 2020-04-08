package com.lineage.server.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IterableElementList implements Iterable<Element> {
    IterableNodeList _list;

    private class MyIterator implements Iterator<Element> {
        private Iterator<Node> _itr;
        private Element _next = null;

        public MyIterator(final Iterator<Node> itr) {
            this._itr = itr;
            this.updateNextElement();
        }

        private void updateNextElement() {
            while (this._itr.hasNext()) {
                final Node node = this._itr.next();
                if (node instanceof Element) {
                    this._next = (Element) node;
                    return;
                }
            }
            this._next = null;
        }

        @Override
        public boolean hasNext() {
            return this._next != null;
        }

        @Override
        public Element next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Element result = this._next;
            this.updateNextElement();
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public IterableElementList(final NodeList list) {
        this._list = new IterableNodeList(list);
    }

    @Override
    public Iterator<Element> iterator() {
        return new MyIterator(this._list.iterator());
    }

}
