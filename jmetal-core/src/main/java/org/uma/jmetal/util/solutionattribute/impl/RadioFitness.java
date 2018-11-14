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

package org.uma.jmetal.util.solutionattribute.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.solutionattribute.DensityEstimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class implements the crowding distance
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class RadioFitness<S extends Solution<?>>
    extends GenericSolutionAttribute<S, Double> implements DensityEstimator<S>{

  /**
   * Assigns crowding distances to all solutions in a <code>SolutionSet</code>.
   *
   * @param solutionList The <code>SolutionSet</code>.
   * @throws org.uma.jmetal.util.JMetalException
   */

  @Override
  public void computeDensityEstimator(List<S> solutionList) {
    int size = solutionList.size();

    
    //Use a new SolutionSet to avoid altering the original solutionSet
    List<S> front = new ArrayList<>(size);
    for (S solution : solutionList) {
      front.add(solution);
    }

    for (int i = 0; i < size; i++) {
      front.get(i).setAttribute(getAttributeID(), 0.0);
    }

    int numberOfObjectives = solutionList.get(0).getNumberOfObjectives() ;
    double objetiveMaxn[]=new double[numberOfObjectives];
    double objetiveMinn[]=new double[numberOfObjectives];
    for(int y=0;y<numberOfObjectives;y++)
    {
      Collections.sort(front, new ObjectiveComparator<S>(y)) ;
      objetiveMinn[y] = front.get(0).getObjective(y);
      objetiveMaxn[y] = front.get(front.size() - 1).getObjective(y);
      front.get(0).setAttribute(getAttributeID(), Double.NEGATIVE_INFINITY);
      front.get(front.size()-1).setAttribute(getAttributeID(), Double.NEGATIVE_INFINITY);
    }
    double distance;
    for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                distance=0.0;
                for(int y = 0; y < numberOfObjectives; y++){
                double val=(front.get(i).getObjective(y)/(objetiveMaxn[y]-objetiveMinn[y])-front.get(j).getObjective(y)/(objetiveMaxn[y]-objetiveMinn[y]));
                distance+=val*val;
            }
            distance=Math.sqrt(distance);
            if(distance<(1.0/size))
            {
                double val = (double)front.get(j).getAttribute(getAttributeID());
                val++;
                front.get(j).setAttribute(getAttributeID(), val);
            }
        }
    
    }


  }

  @Override
  public Object getAttributeID() {
    return this.getClass() ;
  }
}

