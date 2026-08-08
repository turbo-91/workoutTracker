import type {IWorkoutPlanItem} from "./WorkoutPlanItem.ts";

type Day = "DAY1" | "DAY2" | "DAY3" | "DAY4" | "DAY5" | "DAY6" | "DAY7" ;

export interface IWorkoutPlan {
    workoutPlanId: number,
    day: Day,
    name: string;
    items: IWorkoutPlanItem[];
    comment: string;
}
