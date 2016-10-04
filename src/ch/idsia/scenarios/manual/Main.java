/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.*;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

public final class Main {
	public static void main(String[] args) {
		//Option、Taskの設定
		final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
		final BasicTask basicTask = new BasicTask(marioAIOptions);

		//Optionの初期化
		basicTask.setOptionsAndReset(marioAIOptions);
		marioAIOptions.setArgs("-ld 0");

		//Agentの指定
		final Agent agent = new ForwardJumpingAgent();
		marioAIOptions.setAgent(agent);

		//１ゲームスタート
		//basicTask.doEpisodes(1, true, 1);
		basicTask.doEpisodes(5, true, 5);
		System.exit(0);
	}
}
