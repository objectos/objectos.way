/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.util;

import java.util.Collection;

/**
 * A {@link Collection} that does not allow adding nor removing elements.
 *
 * <p>
 * Concrete implementations of this class are required to disallow adding or
 * removing of elements. Any mutator method must throw an
 * {@link UnsupportedOperationException} when invoked.
 *
 * @param <E> type of the elements in this collection
 */
public abstract class UnmodifiableCollection<E> extends AbstractBaseCollection<E> {

	/**
	 * Sole constructor.
	 */
	protected UnmodifiableCollection() {}

	/**
	 * This operation is not supported.
	 *
	 * <p>
	 * This method performs no operation other than throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param e
	 *        ignored (this operation is not supported)
	 *
	 * @return this method does not return as it always throw an exception
	 *
	 * @throws UnsupportedOperationException
	 *         always
	 */
	@Override
	public final boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not supported.
	 *
	 * <p>
	 * This method performs no operation other than throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param c
	 *        ignored (this operation is not supported)
	 *
	 * @return this method does not return as it always throw an exception
	 *
	 * @throws UnsupportedOperationException
	 *         always
	 */
	@Override
	public final boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not supported.
	 *
	 * <p>
	 * This method performs no operation other than throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 *         always
	 */
	@Override
	public final void clear() {
		throw new UnsupportedOperationException();
	}

}