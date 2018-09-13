package io.kpatel.parsers;

import java.util.List;

public interface ListParser<Tkn, T> extends Parser<T,ListStreamParser<Tkn>, List<Tkn>, Tkn> { }
