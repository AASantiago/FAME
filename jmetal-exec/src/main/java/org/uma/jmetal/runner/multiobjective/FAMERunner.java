//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.runner.multiobjective;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import java.io.FileNotFoundException;
import java.util.List;
import org.uma.jmetal.operator.impl.selection.SSDSelection;
import static org.uma.jmetal.runner.AbstractAlgorithmRunner.printFinalSolutionSet;

/**
 * Class to configure and run the FAME algorithm
 *
 * @author Alejandro Santiago <aurelio.santiago@upalt.edu.mx>
 */
public class FAMERunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException
   * Invoking command:
    java org.uma.jmetal.runner.multiobjective.FAMERunner problemName
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

    int populationSize=25,archiveSize=200,maxEvaluations=45000; //
    String problemName = null ;
    if(args.length==0){
      problemName = "org.uma.jmetal.problem.multiobjective.glt.GLT1";
    }else if (args.length == 1) {
      problemName = args[0];
    } 


    problem = ProblemUtils.<DoubleSolution> loadProblem(problemName);
    
    /*THIS OPERATORS ARE NOT USED (THE USED ARE INSIE FAME.java)*/
    double crossoverProbability = 0.9 ;
    double crossoverDistributionIndex = 20.0 ;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;
    ///////////////////////////////////////////////////////////////////////////////////
    
    selection = new SSDSelection<DoubleSolution>(5);//
    //
    algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation)
        .setVariant(NSGAIIBuilder.NSGAIIVariant.FAME)
        .setSelectionOperator(selection)
        .setMaxEvaluations(maxEvaluations)
        .setPopulationSize(populationSize)
        .setArchiveSize(archiveSize)
        .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute() ;

    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
  }
}
