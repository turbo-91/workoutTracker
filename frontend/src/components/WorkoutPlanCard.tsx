import {getWorkoutPlanById} from "../api/workoutPlanApi"
import useSWR from "swr";


export interface WorkoutPlanCardProps {
    id: number
}

// props: Readonly<WorkoutPlanCardProps>

export default function WorkoutPlanCard(props: Readonly<WorkoutPlanCardProps>) {
    const { id } = props;
    const {
        data: workoutPlan,
        error,
        isLoading,
    } = useSWR(
        `/workout-plans/${id}/details`,
        () => getWorkoutPlanById(id),
    );

    if (isLoading) {
        return <p>Loading workout plan...</p>;
    }

    if (error) {
        return <p>No workout plan.</p>;
    }

    if (!workoutPlan) {
        return null;
    }

    return (
        <div
            className="workoutPlan"
        >

            <div className="workoutPlan-details" style={{ padding: '16px', flex: 1 }}>
                <h3 style={{ margin: '0 0 8px' }}>
                    {workoutPlan.name}{' '}
                    <span style={{ color: '#555', fontWeight: 'normal' }}>
           ({workoutPlan.day})
         </span>
                </h3>
                <span style={{ color: '#555', fontWeight: 'normal' }}>
          {workoutPlan.items.map((item, index) => (
              <div key={index}>
                  {item.exerciseName} | {item.targetRepMin} - {item.targetRepMax} Wdh. | {item.targetWeightKg}kg
              </div>
          ))}
         </span>

            </div>
        </div>
    );
}

