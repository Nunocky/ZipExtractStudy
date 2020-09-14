# ZipExtractStudy

Androidで zip圧縮されたファイルからファイルの展開をするテスト

ダウンロードフォルダに置かれた sample.zipを展開し
* その中にある sample.datのMD5ハッシュを計算する
* sample.md5ファイルから MD5ハッシュを取得する
* その2つを比較する

という実験。

## 準備

### sample.zip の作成

    echo "Hello World" > sample.dat
    md5sum sample.dat > sample.md5
    zip -c sample.zip sample.dat sample.md5

これを Android端末の /sdcard/Download/ の下にコピーする。

