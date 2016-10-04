/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import ch.idsia.agents.*;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;


public final class LearningTrack {
	//学習回数を指定(個体数を100としたので、学習回数（世代数）は100)
	final static int LearningSize = 10000;

	private static int evaluateSubmission(MarioAIOptions marioAIOptions,
			LearningAgent learningAgent) {
		/* --------------------------学習-------------------------- */
		// LearningTaskオブジェクトを作成
		LearningTask learningTask = new LearningTask(marioAIOptions);

		// 作ったオブジェクトをLearningAgentのTaskとして渡す
		learningAgent.setLearningTask(learningTask);

		// 学習制限回数を取得
		learningAgent.setEvaluationQuota(LearningTask.getEvaluationQuota());

		// LearningAgentの初期化
		learningAgent.init();

		// 評価のvisualizeをOFF
		marioAIOptions.setVisualization(false);

		// 学習開始（LearningSize回学習を行なう）
		for (int i = 0; i < LearningSize; i++) {
			learningAgent.learn();
			System.out.println("learn :" + (i+1) + "/" + LearningSize + "世代　　");
		}

		/* -------------学習後のBestAgentを用いた評価------------- */
		// getBestAgentメソッドでAgentを取得
		Agent agent = learningAgent.getBestAgent();

		// １トラック終了後にスコアを画面に出力するかどうか
		boolean verbose = true;

		// 評価のvisualizeをON
		marioAIOptions.setVisualization(true);

		// AgentをmarioAIOptionsのAgentにセット
		marioAIOptions.setAgent(agent);

		// BasicTaskで１トラック実行
		BasicTask basicTask = new BasicTask(marioAIOptions);
		basicTask.setOptionsAndReset(marioAIOptions);

		// 1トラック実行（制限時間を超えたらfalse）
		if (!basicTask.runSingleEpisode(1)) {
			System.out.println("MarioAI: out of computational time per action!");
		}

		// 結果を取得する
		EvaluationInfo evaluationInfo = basicTask.getEvaluationInfo();

		// このステージの得点を取得
		int f = evaluationInfo.computeWeightedFitness();

		// verbose=trueなら結果・得点を出力
		if (verbose) {
			System.out.println(evaluationInfo.toString());
			System.out.println("Intermediate SCORE = " + f);
		}

		// Fitnessを返す
		return f;
	}

	public static void main(String[] args) {

		//FPS，visualizeの設定
		int _fps = 24;
		String _vis = "on";
		
		// 学習に用いるAgentを指定
		LearningAgent learningAgent = new LearningWithGA();

		// 学習するステージを生成
		MarioAIOptions marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setVisualization(false);
		//marioAIOptions.setArgs("-ld 0 -ls 0 -lde on");
		marioAIOptions.setArgs("-le off -lhs on -lde on -ld 10 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		
		
		// 学習後の得点をfinalscoreに保存し、画面に出力
		float finalScore = LearningTrack.evaluateSubmission(marioAIOptions,
				learningAgent);
		System.out.println("finalScore = " + finalScore);

		System.exit(0);
	}
}