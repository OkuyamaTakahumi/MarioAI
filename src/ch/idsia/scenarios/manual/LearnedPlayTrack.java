/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;


public class LearnedPlayTrack {
	private static int evaluateSubmission(MarioAIOptions marioAIOptions) {

		// BasicTask�C���X�^���X�쐬
		BasicTask basicTask = new BasicTask(marioAIOptions);

		// �e�X�e�[�W�I�����ƂɃX�R�A����ʂɏo�͂��邩�ǂ���
		boolean verbose = !true;

		// �P�g���b�N���s�i�v�Z�������Ԃ𒴂�����false�j
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

		//MarioAIOptions�C���X�^���X�p�ϐ�
		MarioAIOptions marioAIOptions;

		//FPS�Cvisualize�̐ݒ�
		int _fps = 24;
		String _vis = "on";

		//�X�R�A�ێ��p�ϐ�
		float finalScore = 0;

		// �w�K�ς݂�XML�t�@�C���������Ƃ��đł�����
		//String AgentXML = "src/ch/idsia/scenarios/manual/bestAgent.xml";
		//String AgentXML = "output/GA/LearningWithGA-2016-09-14_21-04-33.xml";
		String AgentXML = "output/GA/LearningWithGA-2016-09-15_12-50-08.xml";


		// Main�@��Level{0, 1, 4, 7, 10}�CSeed{27214098(A), 20358(B)}�C�S���łP�O�X�e�[�W ��


		// Level 0 Seed A
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-le off -lhb off -ld 0 -ls 27214098" +
				" -fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);


		/*
		// Level 0 Seed B
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-le on -lg on -lca on -ld 0 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);
		*/


		/*
		// Level 1 Seed A
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-ld 1 -ls 27214098 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);

		// Level 1 Seed B
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-lde on -ld 1 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);


		// Level 4 Seed A
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-ld 4 -ls 27214098 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);

		// Level 4 Seed B
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-lde on -ld 4 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);


		// Level 7 Seed A
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-lhs on -ld 7 -ls 27214098 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);

		// Level 7 Seed B
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-lde on -lhs on -ld 7 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);
		*/

		/*
		// Level 10 Seed A
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-le off -lhs on -ld 10 -ls 27214098 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);
		*/

		/*
		//Level 10 Seed B
		marioAIOptions = new MarioAIOptions(args);
		marioAIOptions.setAgent(AgentXML);
		marioAIOptions.setArgs("-le off -lhs on -lde on -ld 10 -ls 20358 " +
				"-fps " +_fps+ " -vis " + _vis);
		finalScore += LearnedPlayTrack.evaluateSubmission(marioAIOptions);
		*/


		// �ŏI�X�R�A�o��
		System.out.println("finalScore = " + finalScore);
		System.exit(0);
	}


}
