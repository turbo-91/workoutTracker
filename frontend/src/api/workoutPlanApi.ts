import { apiClient } from "./apiClient.ts";
import type { IWorkoutPlan } from "../models/WorkoutPlan";

export async function getWorkoutPlanById(
    id: number,
): Promise<IWorkoutPlan> {
    const { data } = await apiClient.get<IWorkoutPlan>(
        `/workout-plans/${id}/details`,
    );
    console.log("data in workoutPlanApi: ", data)
    return data;
}