package io.kpatel.parsers.builder;

public interface Builder<Seq, Itm> {
    Builder<Seq, Itm> append(Itm item);

    Seq toOutput();
}
