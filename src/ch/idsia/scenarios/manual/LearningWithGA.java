/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import java.util.Random;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.agents.*;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.utils.wox.serial.Easy;

public class LearningWithGA implements LearningAgent {

	// 1世代あたりの個体数100（世代数を100としたので）
	final int popsize = 1000;

	// 交叉させない個体数2
	final int bestnum = 2;

	// 突然変異率10%,交叉率50%
	final float mutateRate = 0.1f;
	final float crossRate = 0.5f;

	// Task,Agentなどの変数
	private LearningTask task = null;
	private String name = "LearningWithGA";
	private GAAgent[] agents;
	private Agent bestAgent;

	// 評価最大値保持用変数
	float fmax;

	// 乱数用変数r
	Random r = new Random();

	// LearningWithGAコンストラクタ
	public LearningWithGA() {

		// 評価値を初期化
		fmax = 0;

		// 個体数分だけ（今回の場合100個体）Agentを作成
		agents = new GAAgent[popsize];
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new GAAgent();
		}

		// agents[0]をbestAgentとして初期化
		bestAgent = agents[0];
	}

	public void learn() {
		// 100個体の評価
		compFit();
		GAAgent nextagents[] = new GAAgent[popsize];

		// エリート戦略によって2個体残す
		for (int i = 0; i < bestnum; i++) {
			nextagents[i] = agents[i].copy();
		}

		// ルーレット戦略によって選択，交叉
		for (int i = bestnum; i < popsize; i += 2) {
			int[] parentsGene = select();
			cross(nextagents, parentsGene, i);
		}

		// Agentをコピー
		agents = nextagents;

		// 突然変異
		mutate();
	}

	// XMLファイル書き出し
	private void writeFile() {
		String fileName = "output/GA/" + name + "-" 
				+ GlobalOptions.getTimeStamp()	+ ".xml";
		Easy.save(bestAgent, fileName);
	}

	// 個体の評価
	private void compFit() {
		float f = 0.0f;

		// 100個体の評価値を算出
		for (int i = 0; i < agents.length; i++) {
			agents[i].fitness = task.evaluate(agents[i]);
			if (f < agents[i].fitness) {
				f = agents[i].fitness;
				bestAgent = agents[i];
			}
		}

		// fitnessが高い順（降順）にAgentをソート
		for (int i = 0; i < agents.length - 1; i++) {
			for (int j = i + 1; j < agents.length; j++) {
				if (agents[j].fitness > agents[i].fitness) {
					GAAgent temp = agents[i];
					agents[i] = agents[j];
					agents[j] = temp;
				}
			}
		}

		// ファイルに書き出し（最高得点を超えた場合のみ）
		if (fmax < f) {
			fmax = f;
			System.out.println(" fmax = " + fmax);
			writeFile();
		}
	}

	// 交叉の親となる2つの遺伝子をルーレット戦略によって選択
	private int[] select() {

		// totalFitness保持用変数
		float total = 0;
		for (int i = 0; i < popsize; i++) {
			total += agents[i].fitness;
		}

		// 親となる個体番号を保持する変数
		int[] parentsGene = { 0, 0 };

		// 乱数rr,合計点tf
		float rr, tf;

		/* ルーレット戦略 */
		// 親個体1
		rr = r.nextFloat() * total;
		tf = 0;
		for (int i = 0; i < popsize; i++) {
			tf += agents[i].fitness;
			if (rr < tf) {
				parentsGene[0] = i;
				break;
			}
		}

		// 親個体2
		do {
			rr = r.nextFloat() * total;
			tf = 0;
			for (int i = 0; i < popsize; i++) {
				tf += agents[i].fitness;
				if (rr < tf) {
					parentsGene[1] = i;
					break;
				}
			}
		} while (parentsGene[0] != parentsGene[1]);

		return parentsGene;
	}

	// 交叉
	private void cross(GAAgent[] nextagents, int[] parentsGene, int num) {

		// 交叉後の個体保存変数
		nextagents[num] = new GAAgent();
		nextagents[num + 1] = new GAAgent();

		// crossRateの割合で交叉を行なう
		for (int j = 0; j < nextagents[0].gene.length; j++) {
			if (r.nextFloat() < crossRate) {
				nextagents[num].gene[j] = agents[parentsGene[0]].gene[j];
				nextagents[num + 1].gene[j] = agents[parentsGene[1]].gene[j];
			} else {
				nextagents[num].gene[j] = agents[parentsGene[1]].gene[j];
				nextagents[num + 1].gene[j] = agents[parentsGene[0]].gene[j];
			}
		}

	}

	// 突然変異
	private void mutate() {

		// bestAgentを除いて突然変異
		for (int i = 1; i < popsize; i++) {
			for (int j = 0; j < agents[0].gene.length; j++) {

				// mutateRateの割合で突然変異を起こす
				if (r.nextFloat() < mutateRate) {

					// 突然変異後の遺伝子保存用変数
					byte mutatedGene;
					do {
						mutatedGene = (byte) (r.nextFloat() * 
								(1 << Environment.numberOfKeys-1));
					} while (agents[i].gene[j] == mutatedGene);

					// 突然変異後の遺伝子を取得
					agents[i].gene[j] = mutatedGene;
				}
			}
		}
	}

	public void giveReward(float r) {}
	public void newEpisode() {}
	public void setEvaluationQuota(long num) {}
	public void init() {}
	public void giveIntermediateReward(float intermediateReward) {}

	public Agent getBestAgent() {return bestAgent;}
	public void setLearningTask(LearningTask ltask) {task = ltask;}
	public void setName(String n) {name = n;}
	public String getName() {return name;}
	public void reset() {bestAgent.reset();}

	public void setObservationDetails(final int rfWidth, final int rfHeight,
			final int egoRow, final int egoCol) {
		bestAgent.setObservationDetails(rfWidth, rfHeight, egoRow, egoCol);
	}

	public void integrateObservation(Environment env) {
		bestAgent.integrateObservation(env);
	}

	public boolean[] getAction() {
		return bestAgent.getAction();
	}
}