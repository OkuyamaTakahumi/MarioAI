/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import ch.idsia.agents.*;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;


public final class LearningTrack {
	//�w�K�񐔂��w��(�̐���100�Ƃ����̂ŁA�w�K�񐔁i���㐔�j��100)
	final static int LearningSize = 10000;

	private static int evaluateSubmission(MarioAIOptions marioAIOptions,
			LearningAgent learningAgent) {
		/* --------------------------�w�K-------------------------- */
		// LearningTask�I�u�W�F�N�g���쐬
		LearningTask learningTask = new LearningTask(marioAIOptions);

		// ������I�u�W�F�N�g��LearningAgent��Task�Ƃ��ēn��
		learningAgent.setLearningTask(learningTask);

		// �w�K�����񐔂��擾
		learningAgent.setEvaluationQuota(LearningTask.getEvaluationQuota());

		// LearningAgent�̏�����
		learningAgent.init();

		// �]����visualize��OFF
		marioAIOptions.setVisualization(false);

		// �w�K�J�n�iLearningSize��w�K���s�Ȃ��j
		for (int i = 0; i < LearningSize; i++) {
			learningAgent.learn();
			System.out.println("learn :" + (i+1) + "/" + LearningSize + "����@�@");
		}

		/* -------------�w�K���BestAgent��p�����]��------------- */
		// getBestAgent���\�b�h��Agent���擾
		Agent agent = learningAgent.getBestAgent();

		// �P�g���b�N�I����ɃX�R�A����ʂɏo�͂��邩�ǂ���
		boolean verbose = true;

		// �]����visualize��ON
		marioAIOptions.setVisualization(true);

		// Agent��marioAIOptions��Agent�ɃZ�b�g
		marioAIOptions.setAgent(agent);

		// BasicTask�łP�g���b�N���s
		BasicTask basicTask = new BasicTask(marioAIOptions);
		basicTask.setOptionsAndReset(marioAIOptions);

		// 1�g���b�N���s�i�������Ԃ𒴂�����false�j
		if (!basicTask.runSingleEpisode(1)) {
			System.out.println("MarioAI: out of computational time per action!");
		}

		// ���ʂ��擾����
		EvaluationInfo evaluationInfo = basicTask.getEvaluationInfo();

		// ���̃X�e�[�W�̓��_���擾
		int f = evaluationInfo.computeWeightedFitness();

		// verbose=true�Ȃ猋�ʁE���_���o��
		if (verbose) {
			System.out.println(evaluationInfo.toString());
			System.out.println("Intermediate SCORE = " + f);
		}

		// Fitness��Ԃ�
		return f;
	}

	public static void main(String[] args) {

		//FPS�Cvisualize�̐ݒ�
		int _fps = 24;
		String _vis = "on";
		
		// �w�K�ɗp����Agent���w��
		LearningAgent learningAgent = new LearningWithGA();

		// �w�K����X�e�[�W�𐶐�
		MarioAIOptions marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setVisualization(false);
		//marioAIOptions.setArgs("-ld 0 -ls 0 -lde on");
		marioAIOptions.setArgs("-le off -lhs on -lde on -ld 10 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		
		
		// �w�K��̓��_��finalscore�ɕۑ����A��ʂɏo��
		float finalScore = LearningTrack.evaluateSubmission(marioAIOptions,
				learningAgent);
		System.out.println("finalScore = " + finalScore);

		System.exit(0);
	}
}