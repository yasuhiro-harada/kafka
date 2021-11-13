# springBatch

Job - Step - Tasklet - Service - Repositry

チャンクモデル：Jobの任意の状態でコミットできる。途中からやり直せる。
タスクレットモデル：シンプル。一括コミット。

Job : 実行の単位。実行結果がテーブルに保存。

Step：並行処理や直列処理のワークフローの設定の単位

Tasklet：データの取得、加工、送信などの単位で実装
(ItemReader、ItemProcessor、ItemWriter)

Service：実際の処理のビジネスロジック。リポジトリとの疎通。

Repository：DAO層。
