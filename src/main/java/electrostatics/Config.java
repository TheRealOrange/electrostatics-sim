package electrostatics;

import elements.Field;
import elements.Potential;

import java.io.Serializable;

public class Config implements Serializable {
    private int e_fine_compute_distance;

    private double e_fine_precision_adaptive;
    private double e_fine_step_adaptive;
    private double e_rough_step_adaptive;
    private double e_rough_precision_adaptive;

    private double e_fine_step;
    private double e_rough_step;

    private int e_num_steps;

    private int p_num_steps_adaptive;
    private double p_step_amt_adaptive;

    private int p_num_steps;
    private double p_step_amt;

    private double p_tolerance;

    private int p_weight;
    private int p_style;

    private int e_weight;
    private int e_style;

    private int e_num;
    private double p_int;

    private int e_solver_id;
    private int p_solver_id;

    public Config(int e_fine_compute_distance, double e_fine_precision_adaptive, double e_fine_step_adaptive, double e_rough_step_adaptive, double e_rough_precision_adaptive, double e_fine_step, double e_rough_step, int e_num_steps, int p_num_steps_adaptive, double p_step_amt_adaptive, int p_num_steps, double p_step_amt, double p_tolerance, int p_weight, int p_style, int e_weight, int e_style, int e_num, double p_int, int e_solver_id, int p_solver_id) {
        this.e_fine_compute_distance = e_fine_compute_distance;
        this.e_fine_precision_adaptive = e_fine_precision_adaptive;
        this.e_fine_step_adaptive = e_fine_step_adaptive;
        this.e_rough_step_adaptive = e_rough_step_adaptive;
        this.e_rough_precision_adaptive = e_rough_precision_adaptive;
        this.e_fine_step = e_fine_step;
        this.e_rough_step = e_rough_step;
        this.e_num_steps = e_num_steps;
        this.p_num_steps_adaptive = p_num_steps_adaptive;
        this.p_step_amt_adaptive = p_step_amt_adaptive;
        this.p_num_steps = p_num_steps;
        this.p_step_amt = p_step_amt;
        this.p_tolerance = p_tolerance;
        this.p_weight = p_weight;
        this.p_style = p_style;
        this.e_weight = e_weight;
        this.e_style = e_style;
        this.e_num = e_num;
        this.p_int = p_int;
        this.e_solver_id = e_solver_id;
        this.p_solver_id = p_solver_id;
    }

    public Config(SystemModel model) {
        this(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0, 0);
        saveConfig(model);
    }

    public void saveConfig(SystemModel model) {
        e_fine_compute_distance = ElectricFieldLine.getFine_compute_distance();
        e_num_steps = ElectricFieldLine.getNum_steps();

        e_fine_precision_adaptive = ElectricFieldLine.getFine_precision_adaptive();
        e_fine_step_adaptive = ElectricFieldLine.getFine_step_adaptive();
        e_fine_step = ElectricFieldLine.getFine_step();

        e_rough_precision_adaptive = ElectricFieldLine.getRough_precision_adaptive();
        e_rough_step_adaptive = ElectricFieldLine.getRough_step_adaptive();
        e_rough_step = ElectricFieldLine.getRough_step();

        p_num_steps_adaptive = PotentialFieldLine.getNum_steps_adaptive();
        p_num_steps = PotentialFieldLine.getNum_steps();

        p_step_amt_adaptive = PotentialFieldLine.getStep_amt_adaptive();
        p_step_amt = PotentialFieldLine.getStep_amt();

        p_tolerance = PotentialFieldLine.getTolerance();

        e_style = Field.getStyle_num();
        e_weight = Field.getWeight();
        p_style = Potential.getStyle_num();
        p_weight = Potential.getWeight();

        e_num = model.getLinedensity();
        p_int = model.getPotentialint();

        e_solver_id = model.getEfieldsolver_id();
        p_solver_id = model.getUfieldsolver_id();
    }

    public void applyConfig(SystemModel model) {
        ElectricFieldLine.setFine_compute_distance(e_fine_compute_distance);
        ElectricFieldLine.setNum_steps(e_num_steps);

        ElectricFieldLine.setFine_precision_adaptive(e_fine_precision_adaptive);
        ElectricFieldLine.setFine_step_adaptive(e_fine_step_adaptive);
        ElectricFieldLine.setFine_step(e_fine_step);

        ElectricFieldLine.setRough_precision_adaptive(e_rough_precision_adaptive);
        ElectricFieldLine.setRough_step_adaptive(e_rough_step_adaptive);
        ElectricFieldLine.setRough_step(e_rough_step);

        PotentialFieldLine.setNum_steps_adaptive(p_num_steps_adaptive);
        PotentialFieldLine.setNum_steps(p_num_steps);

        PotentialFieldLine.setStep_amt_adaptive(p_step_amt_adaptive);
        PotentialFieldLine.setStep_amt(p_step_amt);

        PotentialFieldLine.setTolerance(p_tolerance);

        model.setLinedensity(e_num);
        model.setPotentialint(p_int);

        model.setEfieldsolver_id(e_solver_id);
        model.setUfieldsolver_id(p_solver_id);
    }

    public int getE_fine_compute_distance() {
        return e_fine_compute_distance;
    }

    public void setE_fine_compute_distance(int e_fine_compute_distance) {
        this.e_fine_compute_distance = e_fine_compute_distance;
    }

    public double getE_fine_precision_adaptive() {
        return e_fine_precision_adaptive;
    }

    public void setE_fine_precision_adaptive(double e_fine_precision_adaptive) {
        this.e_fine_precision_adaptive = e_fine_precision_adaptive;
    }

    public double getE_fine_step_adaptive() {
        return e_fine_step_adaptive;
    }

    public void setE_fine_step_adaptive(double e_fine_step_adaptive) {
        this.e_fine_step_adaptive = e_fine_step_adaptive;
    }

    public double getE_rough_step_adaptive() {
        return e_rough_step_adaptive;
    }

    public void setE_rough_step_adaptive(double e_rough_step_adaptive) {
        this.e_rough_step_adaptive = e_rough_step_adaptive;
    }

    public double getE_rough_precision_adaptive() {
        return e_rough_precision_adaptive;
    }

    public void setE_rough_precision_adaptive(double e_rough_precision_adaptive) {
        this.e_rough_precision_adaptive = e_rough_precision_adaptive;
    }

    public double getE_fine_step() {
        return e_fine_step;
    }

    public void setE_fine_step(double e_fine_step) {
        this.e_fine_step = e_fine_step;
    }

    public double getE_rough_step() {
        return e_rough_step;
    }

    public void setE_rough_step(double e_rough_step) {
        this.e_rough_step = e_rough_step;
    }

    public int getE_num_steps() {
        return e_num_steps;
    }

    public void setE_num_steps(int e_num_steps) {
        this.e_num_steps = e_num_steps;
    }

    public int getP_num_steps_adaptive() {
        return p_num_steps_adaptive;
    }

    public void setP_num_steps_adaptive(int p_num_steps_adaptive) {
        this.p_num_steps_adaptive = p_num_steps_adaptive;
    }

    public double getP_step_amt_adaptive() {
        return p_step_amt_adaptive;
    }

    public void setP_step_amt_adaptive(double p_step_amt_adaptive) {
        this.p_step_amt_adaptive = p_step_amt_adaptive;
    }

    public int getP_num_steps() {
        return p_num_steps;
    }

    public void setP_num_steps(int p_num_steps) {
        this.p_num_steps = p_num_steps;
    }

    public double getP_step_amt() {
        return p_step_amt;
    }

    public void setP_step_amt(double p_step_amt) {
        this.p_step_amt = p_step_amt;
    }

    public double getP_tolerance() {
        return p_tolerance;
    }

    public void setP_tolerance(double p_tolerance) {
        this.p_tolerance = p_tolerance;
    }

    public int getP_weight() {
        return p_weight;
    }

    public void setP_weight(int p_weight) {
        this.p_weight = p_weight;
    }

    public int getP_style() {
        return p_style;
    }

    public void setP_style(int p_style) {
        this.p_style = p_style;
    }

    public int getE_weight() {
        return e_weight;
    }

    public void setE_weight(int e_weight) {
        this.e_weight = e_weight;
    }

    public int getE_style() {
        return e_style;
    }

    public void setE_style(int e_style) {
        this.e_style = e_style;
    }

    public double getE_num() {
        return e_num;
    }

    public void setE_num(int e_num) {
        this.e_num = e_num;
    }

    public double getP_int() {
        return p_int;
    }

    public void setP_int(double p_int) {
        this.p_int = p_int;
    }

    public int getE_solver_id() {
        return e_solver_id;
    }

    public void setE_solver_id(int e_solver_id) {
        this.e_solver_id = e_solver_id;
    }

    public int getP_solver_id() {
        return p_solver_id;
    }

    public void setP_solver_id(int p_solver_id) {
        this.p_solver_id = p_solver_id;
    }
}
